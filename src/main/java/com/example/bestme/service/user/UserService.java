package com.example.bestme.service.user;

import com.example.bestme.domain.user.User;
import com.example.bestme.dto.user.*;
import com.example.bestme.exception.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    // 회원가입
    ResponseEntity<ApiResponse<Void>> join(RequestSignUpDTO to);

    // 로그인

    ResponseEntity<ApiResponse<String>> login(RequestLoginDTO to,
                                              HttpServletResponse response);

    // 비밀번호 재설정 계정 확인
    ResponseEntity<ApiResponse<Long>> identifyUser(RequestIdentifyUserDTO to);

    // 비밀번호 재설정하기
    ResponseEntity<ApiResponse<Void>> resetPassword(RequestResetPasswordDTO to);

    // 회원수정
    ResponseEntity<ApiResponse<String>> modifyUser(RequestModifyUserDTO requestModifyUserDTO,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response);

    // 회원탈퇴
    ResponseEntity<ApiResponse<Void>> deleteUser(RequestDeleteUserDTO requestDeleteUserDTO,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response);

    ResponseEntity<ApiResponse<String>> refresh(HttpServletRequest request);
  
    // user 가져오기
    User getUser(Long userId);

    // user 가져오기 (프론트)
    ResponseEntity<ApiResponse<ResponseUserDTO>> getLoginUser(HttpServletRequest request);
}
