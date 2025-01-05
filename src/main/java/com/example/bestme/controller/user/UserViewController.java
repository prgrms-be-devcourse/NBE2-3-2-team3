package com.example.bestme.controller.user;

import com.example.bestme.service.KakaoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RequiredArgsConstructor
@Controller
public class UserViewController {
    private final KakaoService kakaoService;

    @GetMapping(value = "/login")
    public String login(Model model) {

        model.addAttribute("clientId", kakaoService.getClient_id());
        model.addAttribute("redirectUri", kakaoService.getRedirect_uri());

        return "login";
    }

    @GetMapping("/kakao/callback")
    public String kakaoCallback(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "kakaoCallback";
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

    @GetMapping("/member/modify_profile")
    public String modifyProfile() {
        return "mypage/modify_profile";
    }

    @GetMapping("/member/delete_profile")
    public String deleteProfile() {
        return "mypage/delete_profile";
    }

    @GetMapping( value = { "/member/my_posting", "/member/my_posting/{page}"} )
    public String MyPosting(@PathVariable(required = false) String page, Model model, HttpSession session) {
        // 현재 페이지 초기화
        Integer userPageNumber;
        if (page == null || page.equals("0")) {
            // URL 파라미터가 없을 때, 세션 값 사용 또는 기본값 설정
            userPageNumber = (Integer) session.getAttribute("userPageNumber");
            if (userPageNumber == null) {
                userPageNumber = 1; // 기본값 설정
            }
        } else {
            // URL에서 받은 값을 사용
            userPageNumber = Integer.parseInt(page);
        }
        // 세션에 현재 페이지 저장
        session.setAttribute("userPageNumber", userPageNumber);
        // 모델에 현재 페이지 추가 (뷰에서 사용할 데이터)
        model.addAttribute("userPageNumber", userPageNumber);

        return "mypage/my_posting";
    }
}
