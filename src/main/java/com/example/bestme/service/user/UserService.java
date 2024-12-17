package com.example.bestme.service.user;

import com.example.bestme.dto.user.RequestLoginDTO;
import com.example.bestme.dto.user.RequestSignUpDTO;
import com.example.bestme.exception.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ApiResponse<Void>> join(RequestSignUpDTO to);

    ResponseEntity<ApiResponse<Void>> login(RequestLoginDTO to);
}
