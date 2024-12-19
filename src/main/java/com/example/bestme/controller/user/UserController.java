package com.example.bestme.controller.user;

import com.example.bestme.dto.user.RequestLoginDTO;
import com.example.bestme.dto.user.RequestSignUpDTO;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.user.UserService;
import com.example.bestme.util.jwt.JwtTokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

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
}
