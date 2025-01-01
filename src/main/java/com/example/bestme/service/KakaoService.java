package com.example.bestme.service;

import com.example.bestme.domain.user.Gender;
import com.example.bestme.domain.user.Role;
import com.example.bestme.domain.user.User;
import com.example.bestme.repository.user.UserRepository;
import com.example.bestme.util.jwt.JwtTokenDTO;
import com.example.bestme.util.jwt.JwtTokenProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Slf4j
@Service
public class KakaoService {

    //private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;

    private String client_id;
    private String redirect_uri;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public KakaoService(
            @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
            @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirectUri
    ) {
        this.client_id = clientId;
        this.redirect_uri = redirectUri;
    }



    // 인가 코드를 받아서 accessToken을 반환
    public String getAccessToken(String code){
        System.out.println("인가 코드" + code);
        String accessToken = "";
        String refreshToken = "";
        String reqUrl = "https://kauth.kakao.com/oauth/token";

        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //필수 헤더 세팅
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            //필수 쿼리 파라미터 세팅
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(client_id);
            sb.append("&redirect_uri=").append(redirect_uri);
            sb.append("&code=").append(code);

            System.out.println(sb.toString());

            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            log.info("[KakaoApi.getAccessToken] responseCode = {}", responseCode);

            BufferedReader br;
            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();
            log.info("responseBody = {}", result);


            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();


            br.close();
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return accessToken;
    }

    //accessToken을 받아서 UserInfo 반환
    public HashMap<String, Object> getUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();


            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken); // 요청 헤더에 카카오AccessToken추가
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            int responseCode = conn.getResponseCode();
            log.info("[KakaoApi.getUserInfo] responseCode : {}",  responseCode);

            BufferedReader br;
            if (responseCode >= 200 && responseCode <= 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();
            log.info("responseBody = {}", result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

            userInfo.put("nickname", nickname);
            userInfo.put("email", email);

            br.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }

    //accessToken을 받아서 로그아웃 시키는 메서드
    public void kakaoLogout(String accessToken) {

    }

    @Transactional
    public String handleKakaoLogin(Map<String, Object> userInfo) {
        String email = (String)userInfo.get("email");
        String nickname = (String)userInfo.get("nickname");

        // 이메일 기준으로 사용자 검색
        User user = userRepository.findByEmail(email);

        if (user == null) {
            // 회원가입 처리
            user = new User(
                    null,
                    email,
                    "default_password",
                    nickname,
                    "000000",
                    Gender.U,
                    true,
                    LocalDateTime.now(),
                    null,
                    false,
                    Role.USER
            );
            userRepository.save(user);
        }

        JwtTokenDTO jwtTokenDTO = jwtTokenProvider.generateToken(
                new UsernamePasswordAuthenticationToken(user.getEmail(), "default_password"));

        return "Bearer " + jwtTokenDTO.getAccessToken();
    }

}
