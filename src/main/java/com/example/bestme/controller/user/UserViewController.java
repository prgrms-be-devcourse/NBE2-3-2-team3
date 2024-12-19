package com.example.bestme.controller.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @Value("${kakao.client-id}")
    private String client_id;

    @Value("${kakao.redirect-uri}")
    private String redirect_uri;

    @GetMapping("/login")
    public String loginPage(Model model) {
        // 카카오 로그인 URL 생성
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=" + redirect_uri;
        model.addAttribute("kakaoLoginUrl", location);

        return "login";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }

}
