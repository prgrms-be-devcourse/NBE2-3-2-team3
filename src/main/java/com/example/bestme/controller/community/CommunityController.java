package com.example.bestme.controller.community;

import com.example.bestme.domain.community.Category;
import com.example.bestme.dto.community.CommunityDTO;
import com.example.bestme.dto.community.WriteDTO;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.community.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Community", description = "커뮤니티 관련 API")
@RestController
@RequestMapping("/api")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    // 프론트에서 지정된 category 값이 없으면, 해당 필드 자체를 요청에 포함X ( 빈 값도 안됨 )
    @Operation( summary = "게시물 생성", description = "지정된 카테고리가 없을 경우 기본 값 또는 키를 제거해주세요." )
    @PostMapping(value = "/community/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CommunityDTO>> communityWrite(
            @RequestPart( name = "to") WriteDTO to, // 데이터 전송
            @RequestParam( name = "file", required = false) MultipartFile file // 파일 업로드 처리
    ) {

        CommunityDTO result = communityService.write(to, file);

        return ResponseEntity.ok(ApiResponse.success("게시물 생성 완료", result));
    }

    @Operation( summary = "파일 업로드", description = "스웨거 파일 업로드 테스트" )
    @PostMapping(value = "/community/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> fileUpload( @RequestParam( name = "file", required = false) MultipartFile file ) {

        String fileName;

        try {
            fileName = communityService.fileUpload(file);
        } catch (IOException e) {
            throw new RuntimeException("[에러]" + e.getMessage());
        }

        return ResponseEntity.ok(ApiResponse.success("게시물 생성 완료", fileName));
    }
}
