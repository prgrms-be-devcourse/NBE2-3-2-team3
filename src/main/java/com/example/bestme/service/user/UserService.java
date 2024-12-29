package com.example.bestme.service.user;

import com.example.bestme.domain.Color;
import com.example.bestme.domain.user.User;
import com.example.bestme.dto.user.RequestLoginDTO;
import com.example.bestme.dto.user.RequestSignUpDTO;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.util.jwt.JwtTokenDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    // 회원가입
    ResponseEntity<ApiResponse<Void>> join(RequestSignUpDTO to);

    // 로그인
    ResponseEntity<ApiResponse<JwtTokenDTO>> login(RequestLoginDTO to, HttpServletResponse response);

    // user 가져오기
    User getUser(Long userId);
}
