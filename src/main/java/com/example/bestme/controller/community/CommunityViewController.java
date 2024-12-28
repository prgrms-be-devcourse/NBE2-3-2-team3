package com.example.bestme.controller.community;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommunityViewController {

    // 커뮤니티 메인 view
    @RequestMapping( "/community/{page}" )
    public String communityMain(@PathVariable(required = false) String page, Model model, HttpSession session) {
        // 현재 페이지 초기화
        String currentPage;

        if (page == null) {
            // URL 파라미터가 없을 때, 세션 값 사용 또는 기본값 설정
            currentPage = (String) session.getAttribute("currentPage");
            if (currentPage == null) {
                currentPage = "1"; // 기본값 설정
            }
        } else {
            // URL에서 받은 값을 사용
            currentPage = page;
        }

        // 세션에 현재 페이지 저장
        session.setAttribute("currentPage", currentPage);

        // 모델에 현재 페이지 추가 (뷰에서 사용할 데이터)
        model.addAttribute("currentPage", currentPage);

        return "communityMain";
    }

    // 게시물 디테일 view
    @RequestMapping( "/community/detail/{boardId}" )
    public String communityDetail(@PathVariable String boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "communityDetail";
    }

    // 게시물 생성 view
    @RequestMapping( "/community/write" )
    public String communityWrite() {
        return "communityWrite";
    }

    // 게시물 수정 view
    @RequestMapping( "/community/update/{boardId}" )
    public String communityUpdate(@PathVariable String boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "communityUpdate";
    }

    // 게시물 삭제 view
    @RequestMapping( "/community/delete/{boardId}" )
    public String communityDelete(@PathVariable String boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "communityDelete";
    }
}
