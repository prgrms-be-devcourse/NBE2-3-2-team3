package com.example.bestme.controller.user;

import com.example.bestme.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserViewController {
    private final KakaoService kakaoService;

    @GetMapping(value = "/login")
    public String login(Model model) {

        //String kakaoUrl = kakaoService.getKakaoLogin();
        //log.info("&&&Kakao Login URL: {}", kakaoUrl);
        model.addAttribute("clientId", kakaoService.getClient_id());
        model.addAttribute("redirectUri", kakaoService.getRedirect_uri());

        return "login";
    }

    /*
    @RequestMapping("/login/oauth2/code/kakao")
    public String kakaoLogin(@RequestParam String code){
        System.out.println("[UserViewController] kakaoLogin함수");
        // 1. 인가 코드 받기 (@RequestParam String code)

        // 2. 토큰 받기
        String accessToken = kakaoService.getAccessToken(code);

        // 3. 사용자 정보 받기
        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

        String email = (String)userInfo.get("email");
        String nickname = (String)userInfo.get("nickname");

        System.out.println("email = " + email);
        System.out.println("nickname = " + nickname);
        System.out.println("accessToken = " + accessToken);

        return "redirect:/main";
    }

     */

    /*

    // 카카오 로그인 처리
    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<Map<String, String>> kakaoLogin(@RequestParam String code) {
        // 1. 카카오 액세스 토큰 가져오기
        String accessToken = kakaoService.getAccessToken(code);

        // 2. 사용자 정보 가져오기
        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

        String email = (String)userInfo.get("email");
        String nickname = (String)userInfo.get("nickname");

        // 값 확인
        System.out.println("email = " + email);
        System.out.println("nickname = " + nickname);
        System.out.println("accessToken = " + accessToken);

        // 3. 로그인/회원가입 처리 및 JWT 토큰 생성
        String jwtToken = kakaoService.handleKakaoLogin(userInfo);

        // 4. 클라이언트에 JWT 토큰 반환
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);

        return ResponseEntity.ok(response);
    }

    */

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/join")
    public String getJoin() {
        return "join";
    }

    @GetMapping("/resetPassword")
    public String resetPassword() {
        return "resetPassword";
    }
}
