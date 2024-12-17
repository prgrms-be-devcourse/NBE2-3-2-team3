package com.example.bestme.controller.community;

import com.example.bestme.dto.community.CategoryCode;
import com.example.bestme.dto.community.CommunityTO;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.community.CommunityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping( "/testInsert" )
    public ResponseEntity<ApiResponse<String>> insertCommunity() {

        String result = "테스트 데이터 생성 완료";

        CommunityTO to1 = new CommunityTO();
//        to1.setBoard_id(0L);
        to1.setUser_id(1001L);
        to1.setSubject("테스트 제목");
        to1.setImagename("테스트 이미지 파일명");
        to1.setContent("테스트 내용");
        to1.setView(0L);
        to1.setCategory(CategoryCode.SPRING);

        CommunityTO to2 = new CommunityTO();
//        to2.setBoard_id(0L);
        to2.setUser_id(1002L);
        to2.setSubject("테스트 제목2");
        to2.setImagename("테스트 이미지 파일명2");
        to2.setContent("테스트 내용2");
        to2.setView(0L);
        to2.setCategory(CategoryCode.SPRING);

        communityService.insert(to1);
        communityService.insert(to2);

        return ResponseEntity.ok(ApiResponse.success("삽입 성공", result));
    }
}
