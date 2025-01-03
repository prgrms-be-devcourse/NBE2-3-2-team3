package com.example.bestme.controller.user;

import com.example.bestme.domain.user.User;
import com.example.bestme.dto.user.*;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.KakaoService;
import com.example.bestme.service.user.UserService;
import com.example.bestme.util.jwt.JwtAuthenticationFilter;
import com.example.bestme.util.jwt.JwtTokenDTO;
import com.example.bestme.util.jwt.JwtTokenProvider;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "User", description = "회원 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    private final KakaoService kakaoService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final LocalDateTime localDateTime = LocalDateTime.now();

    @Operation( summary = "회원가입 API 입니다.", description = "새로 회원가입하는 API 입니다." )
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<Void>> join(@RequestBody RequestSignUpDTO to) {
        return userService.join(to);
    }

    @Operation( summary = "로그인 API 입니다.", description = "회원으로 가입되어 있는 유저에 관한 로그인 API" )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody RequestLoginDTO to, HttpServletResponse response) {
        return userService.login(to, response);
    }

    @Operation( summary = "비밀번호 재설정 기능에서 회원을 확인하는 API 입니다.", description = "이메일과 생년월일 입력으로 계정확인" )
    @PostMapping("/identifyUser")
    public ResponseEntity<ApiResponse<Long>> identifyUser(@RequestBody RequestIdentifyUserDTO to){
        return userService.identifyUser(to);
    }

    @Operation( summary = "비밀번호 재설정 API 입니다.", description = "비밀번호 형식에 맞춰서 비밀번호 입력." )
    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody RequestResetPasswordDTO to){
        return userService.resetPassword(to);
    }

    // 카카오 로그인 처리
    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<Map<String, String>> kakaoLogin(
            @RequestParam String code,
            HttpServletRequest request, Model model) {
        System.out.println("[UserController] kakaoLogin() 실행 ");

        String existingToken = jwtTokenProvider.resolveToken(request);
        if(existingToken != null) {
            System.out.println("이미 token 있는 회원");
//            return ResponseEntity.ok(Map.of("token", existingToken));
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "/kakao/callback?token=" + existingToken)
                    .build();
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
        response.put("email", email);

//        return ResponseEntity.ok(response);
        model.addAttribute("token", jwtToken);  // 모델에 토큰 추가
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/kakao/callback?token=" + jwtToken)
                .build();
    }


    @Operation( summary = "로그아웃 API 입니다.", description = "로그아웃을 통해 token 삭제" )
    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        jwtTokenProvider.deleteRefreshToken(response);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, "로그아웃에 성공하였습니다.", null));
    }

    @Operation( summary = "기존 refresh 토큰을 이용하여 새로운 accessToken 발급 API 입니다.", description = "refresh 토큰이 만료되어 있다면 기존 저장된 토큰 삭제 및 로그인 페이지로 리다이렉트" )
    @GetMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(HttpServletRequest request) {
        return userService.refresh(request);
    }

    @Operation( summary = "회원 정보 수정 API 입니다.", description = "accessToken 및 기존 비밀번호 입력(필수), 나머지 항목들은 변경있을 시 입력(선택)" )
    @PutMapping("/user")
    public ResponseEntity<ApiResponse<String>> modifyUser(@RequestBody RequestModifyUserDTO requestModifyUserDTO,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) {
        return userService.modifyUser(requestModifyUserDTO, request, response);
    }

    @Operation( summary = "회원탈퇴 API 입니다.", description = "accessToken 및 기존 비밀번호 입력을 통해 회원탈퇴" )
    @DeleteMapping("/user")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@RequestBody RequestDeleteUserDTO requestDeleteUserDTO,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {
        return userService.deleteUser(requestDeleteUserDTO, request, response);
    }

    @Operation( summary = "로그인 중인 회원에 관한 정보를 가져오는 API 입니다.", description = "accessToken 을 활용하여 userId, nickname, birth, gender 반환" )
    @GetMapping("/loginUser")
    public ResponseEntity<ApiResponse<ResponseUserDTO>> getLoginUser(HttpServletRequest request) {
        return userService.getLoginUser(request);
    }


/*
    @GetMapping("/login/kakao")
    public ResponseEntity<ApiResponse<String>> getKakaoLoginUrl() {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=" + redirect_uri;
        return ResponseEntity.ok(ApiResponse.success(location));
    }

 */

    @Operation(summary = "서버 재시작 감지 API 입니다.", description = "서버 시작 시간을 반환하여 그 값에 따라 토큰을 삭제합니다.")
    @GetMapping("/isRestartedServer")
    public String isRestartedServer() {
        return localDateTime.toString();
    }
}
