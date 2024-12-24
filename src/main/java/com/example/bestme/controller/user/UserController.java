package com.example.bestme.controller.user;

import com.example.bestme.dto.user.RequestLoginDTO;
import com.example.bestme.dto.user.RequestSignUpDTO;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.user.UserService;
import com.example.bestme.util.jwt.JwtTokenDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<Void>> join(@RequestBody RequestSignUpDTO to) {
        return userService.join(to);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtTokenDTO>> login(@RequestBody RequestLoginDTO to, HttpServletResponse response) {
        return userService.login(to, response);
    }
}
