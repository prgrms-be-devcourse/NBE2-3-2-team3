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
import java.util.List;

@Tag(name = "Community", description = "커뮤니티 관련 API")
@RestController
@RequestMapping("/api")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    // 게시물 생성 응답 메서드
    @Operation( summary = "게시물 생성"
            , description = "지정된 카테고리가 없을 경우 필드를 제거해주세요.<br> imagename 필드는 지워주세요." )
    @PostMapping(value = "/community/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CommunityDTO>> communityWrite(
            @RequestPart(name = "to") WriteDTO to, // 데이터 전송 (JSON 형식)
            @RequestPart(name = "file", required = false) MultipartFile file // 파일 업로드 처리
    ) {

        CommunityDTO result = communityService.createBoard(to, file);

        return ResponseEntity.ok(ApiResponse.success("게시물 생성 완료", result));
    }

    // 모든 게시물 반환 메서드 ( 페이징 기능 추가 )
    @GetMapping( "/findAllPage" )
    public ResponseEntity<ApiResponse<List<CommunityDTO>>> findAllPage() {

        List<CommunityDTO> result = communityService.findAllPage();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 스웨거 요청 데이터 타입 오류 테스트용 API ( 결과 : 파일 정상, DTO 비정장 - 값이 null )
    /*
    @Operation( summary = "게시물 생성", description = "지정된 카테고리가 없을 경우 기본 값 또는 키를 제거해주세요." )
    @PostMapping(value = "/community/write1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> communityWrite1(
            @ModelAttribute( name = "to" ) WriteDTO to, // 데이터 전송
            @RequestPart( name = "file", required = false) MultipartFile file // 파일 업로드 처리
    ) {
        return ResponseEntity.ok(ApiResponse.success("게시물 생성 완료", "result"));
    }
     */

    // 파일 업로드 기능 테스트
    /*
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
     */
}
