package com.example.bestme.controller.user;

import com.example.bestme.dto.user.RequestLoginDTO;
import com.example.bestme.dto.user.RequestSignUpDTO;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.user.UserService;
import com.example.bestme.util.jwt.JwtTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${kakao.client-id}")
    private String client_id;

    @Value("${kakao.redirect-uri}")
    private String redirect_uri;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<Void>> join(@RequestBody RequestSignUpDTO to) {
        return userService.join(to);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtTokenDTO>> login(@RequestBody RequestLoginDTO to) {
        return userService.login(to);
    }

    @PostMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/login/kakao")
    public ResponseEntity<ApiResponse<String>> getKakaoLoginUrl() {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=" + redirect_uri;
        return ResponseEntity.ok(ApiResponse.success(location));
    }

}
