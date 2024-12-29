package com.example.bestme.controller.user;

import com.example.bestme.dto.user.RequestLoginDTO;
import com.example.bestme.dto.user.RequestIdentifyUserDTO;
import com.example.bestme.dto.user.RequestResetPasswordDTO;
import com.example.bestme.dto.user.RequestSignUpDTO;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.KakaoService;
import com.example.bestme.service.user.UserService;
import com.example.bestme.util.jwt.JwtAuthenticationFilter;
import com.example.bestme.util.jwt.JwtTokenDTO;
import com.example.bestme.util.jwt.JwtTokenProvider;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<Void>> join(@RequestBody RequestSignUpDTO to) {
        return userService.join(to);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody RequestLoginDTO to, HttpServletResponse response) {
        return userService.login(to, response);
    }

    @PostMapping("/identifyUser")
    public ResponseEntity<ApiResponse<Long>> resetPassword(@RequestBody RequestIdentifyUserDTO to){
        return userService.identifyUser(to);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody RequestResetPasswordDTO to){
        System.out.println(to.getUserId());
        return userService.resetPassword(to);
    }

    // 카카오 로그인 처리
    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<Map<String, String>> kakaoLogin(
            @RequestParam String code,
            HttpServletRequest request) {
        System.out.println("[UserController] kakaoLogin() 실행 ");

        String existingToken = jwtTokenProvider.resolveToken(request);
        if(existingToken != null) {
            System.out.println("이미 token 있는 회원");
            return ResponseEntity.ok(Map.of("token", existingToken));
        }

        System.out.println("신규 회원. token 발급");
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

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        jwtTokenProvider.deleteRefreshToken(response);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, "로그아웃에 성공하였습니다.", null));
    }

    @GetMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(HttpServletRequest request, HttpServletResponse response) {
        return userService.refresh(request, response);
    }


    @DeleteMapping("/user")
    public ResponseEntity<ApiResponse<Void>> deleteUser(HttpServletRequest request) {
        return userService.deleteUser(request);
    }


/*
    @GetMapping("/login/kakao")
    public ResponseEntity<ApiResponse<String>> getKakaoLoginUrl() {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=" + redirect_uri;
        return ResponseEntity.ok(ApiResponse.success(location));
    }

 */


}
