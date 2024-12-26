package com.example.bestme.controller.user;

import com.example.bestme.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserViewController {
    private final KakaoService kakaoService;

    @GetMapping(value = "/login")
    public String login(Model model) {

        //String kakaoUrl = kakaoService.getKakaoLogin();
        //log.info("&&&Kakao Login URL: {}", kakaoUrl);
        model.addAttribute("clientId", kakaoService.getClient_id());
        model.addAttribute("redirectUri", kakaoService.getRedirect_uri());

        System.out.println("[clientID] " + kakaoService.getClient_id());
        System.out.println("[redirectUri] " + kakaoService.getRedirect_uri());

        return "login";
    }


    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/join")
    public String getJoin() {
        return "join";
    }

    @GetMapping("/member")
    public String member() {
        return "member";
    }

    @GetMapping("/member/my_posting")
    public String MyPosting() {
        return "member/my_posting";
    }

}
