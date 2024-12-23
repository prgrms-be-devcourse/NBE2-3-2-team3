package com.example.bestme.controller.user;

import com.example.bestme.dto.user.RequestLoginDTO;
import com.example.bestme.dto.user.RequestSignUpDTO;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.KakaoService;
import com.example.bestme.service.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    private final KakaoService kakaoService;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<Void>> join(@RequestBody RequestSignUpDTO to) {
        return userService.join(to);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(@RequestBody RequestLoginDTO to, HttpServletResponse response) {
        return userService.login(to, response);
    }

    @PostMapping("/test")
    public String test() {
        return "test";
    }

    // 카카오 로그인 처리
    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<Map<String, String>> kakaoLogin(@RequestParam String code) {
        System.out.println("[UserController] kakaoLogin() 실행 ");
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


/*
    @GetMapping("/login/kakao")
    public ResponseEntity<ApiResponse<String>> getKakaoLoginUrl() {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=" + redirect_uri;
        return ResponseEntity.ok(ApiResponse.success(location));
    }

 */


}
