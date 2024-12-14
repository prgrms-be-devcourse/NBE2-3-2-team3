package com.example.bestme.controller;

import com.example.bestme.dto.UserResponse;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/user")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@RequestParam Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User found", user));
    }

    //    에러응답 예시
    @GetMapping("/api/error")
    public ResponseEntity<ApiResponse<Void>> errorResponse() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Invalid Request", HttpStatus.BAD_REQUEST));
    }

}

