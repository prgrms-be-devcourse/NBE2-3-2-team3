package com.example.bestme.service.user;

import com.example.bestme.dto.user.RequestLoginDTO;
import com.example.bestme.dto.user.RequestIdentifyUserDTO;
import com.example.bestme.dto.user.RequestResetPasswordDTO;
import com.example.bestme.dto.user.RequestSignUpDTO;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.util.jwt.JwtTokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    // 회원가입
    ResponseEntity<ApiResponse<Void>> join(RequestSignUpDTO to);

    // 로그인
    ResponseEntity<ApiResponse<String>> login(RequestLoginDTO to, HttpServletResponse response);

    // 비밀번호 재설정 계정 확인
    ResponseEntity<ApiResponse<Long>> identifyUser(RequestIdentifyUserDTO to);

    // 비밀번호 재설정하기
    ResponseEntity<ApiResponse<Void>> resetPassword(RequestResetPasswordDTO to);

    // 회원탈퇴
    ResponseEntity<ApiResponse<Void>> deleteUser(HttpServletRequest request);

    ResponseEntity<ApiResponse<String>> refresh(HttpServletRequest request, HttpServletResponse response);
}
