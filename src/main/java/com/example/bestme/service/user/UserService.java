package com.example.bestme.service.user;

import com.example.bestme.dto.user.RequestLoginDTO;
import com.example.bestme.dto.user.RequestSignUpDTO;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.util.jwt.JwtTokenDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    // 회원가입
    ResponseEntity<ApiResponse<Void>> join(RequestSignUpDTO to);

    // 로그인
    ResponseEntity<ApiResponse<JwtTokenDTO>> login(RequestLoginDTO to);
}
