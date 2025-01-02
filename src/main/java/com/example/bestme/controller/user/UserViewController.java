package com.example.bestme.controller.user;

import com.example.bestme.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

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

    @GetMapping("/resetPassword")
    public String resetPassword() {
        return "resetPassword";
    }

    @GetMapping("/member")
    public String member() {
        return "mypage/member";
    }

    @GetMapping("/member/my_posting")
    public String MyPosting(Model model) {
        // DB에서 자기가 쓴 게시물 불러와서
        // model.addAttribute로 넘겨줘야할듯
        return "mypage/my_posting";
    }

    @GetMapping("/member/modify_profile")
    public String modifyProfile() {
        return "mypage/modify_profile";
    }

    @GetMapping("/member/delete_profile")
    public String deleteProfile() {
        return "mypage/delete_profile";
    }
}
