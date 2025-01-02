package com.example.bestme.controller.community;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommunityViewController {

    // 커뮤니티 메인 view
    @GetMapping( value = {"/community", "/community/{page}"} )
    public String communityMain(@PathVariable(required = false) String page, Model model, HttpSession session) {
        // 현재 페이지 초기화
        Integer currentPageNumber;

        if (page == null || page.equals("0")) {
            // URL 파라미터가 없을 때, 세션 값 사용 또는 기본값 설정
            currentPageNumber = (Integer) session.getAttribute("currentPageNumber");
            if (currentPageNumber == null) {
                currentPageNumber = 1; // 기본값 설정
            }
        } else {
            // URL에서 받은 값을 사용
            currentPageNumber = Integer.parseInt(page);
        }

        // 세션에 현재 페이지 저장
        session.setAttribute("currentPageNumber", currentPageNumber);

        // 모델에 현재 페이지 추가 (뷰에서 사용할 데이터)
        model.addAttribute("currentPageNumber", currentPageNumber);

        return "community/community_main";
    }

    // 게시물 디테일 view
    @GetMapping( "/community/detail/{boardId}" )
    public String communityDetail(@PathVariable String boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "community/community_detail";
    }

    // 게시물 생성 view
    @GetMapping( "/community/write" )
    public String communityWrite() {
        return "community/community_write";
    }

    // 게시물 수정 view
    @GetMapping( "/community/modify/{boardId}" )
    public String communityUpdate(@PathVariable String boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "community/community_modify";
    }

    // 게시물 삭제 view
    @GetMapping( "/community/delete/{boardId}" )
    public String communityDelete(@PathVariable String boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "community/community_delete";
    }
}
