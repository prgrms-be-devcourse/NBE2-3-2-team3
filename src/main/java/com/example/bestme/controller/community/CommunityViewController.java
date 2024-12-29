package com.example.bestme.controller.community;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommunityViewController {

    // 커뮤니티 메인 view
    @RequestMapping( value = {"/community", "/community/{page}"} )
    public String communityMain(@PathVariable(required = false) String page, Model model, HttpSession session) {
        // 현재 페이지 초기화
        String currentPageNumber;

        if (page == null || page.equals("0")) {
            // URL 파라미터가 없을 때, 세션 값 사용 또는 기본값 설정
            currentPageNumber = (String) session.getAttribute("currentPageNumber");
            if (currentPageNumber == null) {
                currentPageNumber = "1"; // 기본값 설정
            }
        } else {
            // URL에서 받은 값을 사용
            currentPageNumber = page;
        }

        // 세션에 현재 페이지 저장
        session.setAttribute("currentPageNumber", currentPageNumber);

        // 모델에 현재 페이지 추가 (뷰에서 사용할 데이터)
        model.addAttribute("currentPageNumber", currentPageNumber);

        System.out.println(currentPageNumber);

        return "community/community_main";
    }

    // 게시물 디테일 view
    @RequestMapping( "/community/detail/{boardId}" )
    public String communityDetail(@PathVariable String boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "community/community_detail";
    }

    // 게시물 생성 view
    @RequestMapping( "/community/write" )
    public String communityWrite() {
        return "community/community_write";
    }

    // 게시물 수정 view
    @RequestMapping( "/community/modify/{boardId}" )
    public String communityUpdate(@PathVariable String boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "community/community_modify";
    }

    // 게시물 삭제 view
    @RequestMapping( "/community/delete/{boardId}" )
    public String communityDelete(@PathVariable String boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "community/community_delete";
    }

    // 현재 User가 작성한 게시물 목록 view
    @RequestMapping( "/community/my_boards/{page}" )
    public String communityMyBoards(@PathVariable(required = false) String page, Model model, HttpSession session) {

        // 현재 페이지 초기화
        String userPageNumber;

        if (page == null) {
            // URL 파라미터가 없을 때, 세션 값 사용 또는 기본값 설정
            userPageNumber = (String) session.getAttribute("userPageNumber");
            if (userPageNumber == null) {
                userPageNumber = "1"; // 기본값 설정
            }
        } else {
            // URL에서 받은 값을 사용
            userPageNumber = page;
        }

        // 세션에 현재 페이지 저장
        session.setAttribute("userPageNumber", userPageNumber);

        // 모델에 현재 페이지 추가 (뷰에서 사용할 데이터)
        model.addAttribute("userPageNumber", userPageNumber);

        return "my_posting";
    }
}
