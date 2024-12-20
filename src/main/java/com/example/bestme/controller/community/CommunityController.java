package com.example.bestme.controller.community;

import com.example.bestme.dto.community.ResponseAllBoardDTO;
import com.example.bestme.dto.community.RequestWriteDTO;
import com.example.bestme.dto.community.ResponseFindBoardDTO;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.community.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            , description = "지정된 카테고리가 없을 경우 필드를 제거해주세요.<br> imagename 및 view 필드는 지워주세요." )
    @PostMapping(value = "/community/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ResponseFindBoardDTO>> communityWrite(
            @RequestPart(name = "to") RequestWriteDTO to, // 데이터 전송 (JSON 형식)
            @RequestPart(name = "file", required = false) MultipartFile file // 파일 업로드 처리
    ) {

        ResponseFindBoardDTO result = communityService.createBoard(to, file);

        return ResponseEntity.ok(ApiResponse.success("게시물 생성 완료", result));
    }

    // 모든 게시물 반환 메서드 ( 페이징 기능 추가 )
    @GetMapping( "/findAllBoard" )
    public ResponseEntity<ApiResponse<List<ResponseAllBoardDTO>>> findAllBoard() {

        List<ResponseAllBoardDTO> result = communityService.findAllBoard();

        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
