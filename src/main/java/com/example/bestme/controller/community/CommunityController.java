package com.example.bestme.controller.community;

import com.example.bestme.dto.community.*;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.community.CommunityService;
import com.example.bestme.service.community.LocalFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
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
    private final PagedResourcesAssembler<ResponseAllBoardDTO> assembler;
    private final LocalFileService localFileService;

    public CommunityController(CommunityService communityService, PagedResourcesAssembler<ResponseAllBoardDTO> assembler
            , LocalFileService localFileService) {
        this.communityService = communityService;
        this.assembler = assembler;
        this.localFileService = localFileService;
    }

    @Operation( summary = "테스트용 게시물 생성(테스트 순서: 1)", description = "55개의 게시물이 자동 생성됩니다" )
    @PostMapping( "/community/testCreate")
    public ResponseEntity<ApiResponse<String>> testCreate() {
        int count = 0;
        for( int i=0; i < 55; i++) {
            RequestWriteDTO to = new RequestWriteDTO();
            to.setUserId(Long.valueOf(i));
            to.setSubject("제목 " + i);
            to.setContent("내용 " + i);
            MultipartFile file = null;
            communityService.createBoard(to, file);
            count++;
        }

        String result = "테스트 데이터 " + count + " 개 생성 완료";

        return ResponseEntity.ok(ApiResponse.success("게시물 생성 완료", result));
    }

    // 게시물 생성 응답 메서드
    @Operation( summary = "게시물 생성(테스트 순서: 1)"
            , description = "지정된 카테고리가 없을 경우 필드를 제거해주세요.<br> imagename 및 view 필드는 지워주세요." )
    @PostMapping(value = "/community/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ResponseFindBoardDTO>> communityWrite(
            @RequestPart(name = "to") RequestWriteDTO to, // 데이터 전송 (JSON 형식)
            @RequestPart(name = "file", required = false) MultipartFile file // 파일 업로드 처리
    ) {

        ResponseFindBoardDTO result = communityService.createBoard(to, file);

        return ResponseEntity.ok(ApiResponse.success("게시물 생성 완료", result));
    }

    // 게시물 목록 응답 메서드 ( 페이징 기능 추가 )
    @Operation( summary = "게시물 목록(테스트 순서: 2)", description = "시작 페이지 번호 1" )
    @GetMapping( "/community/{page}" )
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<ResponseAllBoardDTO>>>> findAllBoard(
            @Parameter(description = "페이지 번호", example = "1")
            @PathVariable Integer page)
    {
        // page 요청 DTO 객체 생성
        RequestPageDTO pageDTO = new RequestPageDTO(page);
        int currentPage = pageDTO.getCurrentPage();
        int numberOfDataPerPage = pageDTO.getNumberOfDataPerPage();
        Page<ResponseAllBoardDTO> results = communityService.findAllBoard(currentPage, numberOfDataPerPage);

        // PagedModel을 사용하여 페이지화된 데이터 래핑
        PagedModel<EntityModel<ResponseAllBoardDTO>> pagedModel = assembler
                .toModel(results, result
                        -> EntityModel.of(result, Link.of("/community/detail/" + result.getBoardId())));

        // 기존적으로 자동 생성되는 링크 제거
        pagedModel.removeLinks();
        // page 요청 DTO 객체 업데이트 및 업데이트 필드 데이터 호출
        pageDTO.setNumberPageGroup(results.getTotalPages());
        int originPage = pageDTO.getOriginPage();
        int startPage = pageDTO.getStartPageOfGroup();
        int lastPage = pageDTO.getLastPageOfGroup();
        int numberOfPagesPerGroup = pageDTO.getNumberOfPagesPerGroup();

        // 현재 페이지에 따라 링크 수정
        Link selfLink = Link.of("/community/" + originPage).withSelfRel();
        // 'prev' 링크가 첫 페이지를 넘지 않게 처리
        Link prevLink = null;
        if( !results.isFirst() ) prevLink = Link.of("/community/" + (originPage - 1) ).withRel("prevPage");
        // 'next' 링크는 마지막 페이지를 넘지 않게 처리
        Link nextLink = null;
        if( !results.isLast() ) nextLink = Link.of("/community/" + (originPage + 1)).withRel("nextPage");
        // 이전 페이지 그룹의 첫 페이지 링크
        Link firstLink = Link.of("/community/1").withRel("firstPageOfPrevGroup");
        // 현재 페이지 그룹의 첫 페이지 번호가 1이 아닐 경우 -> 이전 페이지 그룹의 첫 페이지로 이동
        if( startPage > 1 ) firstLink = Link.of("/community/" + (startPage - numberOfPagesPerGroup) ).withRel("firstPageOfPrevGroup");
        // 이후 페이지 그룹의 첫 페이지 링크
        Link lastLink = Link.of("/community/" + results.getTotalPages()).withRel("lastPageOfNextGroup");
        // 현재 페이지 그룹의 마지막 페이지 번호가 총 페이지 갯수가 아닐 경우 -> 다음 페이지 그룹의 첫 페이지로 이동
        if( lastPage < results.getTotalPages() ) lastLink = Link.of("/community/" + (startPage + numberOfPagesPerGroup)).withRel("lastPageOfNextGroup");

        // 기존 링크를 수정된 링크로 교체
        if( prevLink != null ) pagedModel.add(prevLink);
        pagedModel.add(selfLink);
        if( nextLink != null ) pagedModel.add(nextLink);
        pagedModel.add(firstLink);
        pagedModel.add(lastLink);

        // ApiResponse에 PagedModel을 넣어서 반환
        return ResponseEntity.ok(ApiResponse.success(pagedModel));
    }

    @Operation( summary = "게시물 상세보기(테스트 순서: 2)", description = "특정 게시물의 세부 내용 확인 ( 이미지 파일 제외 )" )
    @GetMapping( "/community/detail/{boardId}" )
    public ResponseEntity<ApiResponse<ResponseFindBoardDTO>> communityDetail(@PathVariable String boardId) {

        ResponseFindBoardDTO result = communityService.findBoardById(boardId);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation( summary = "게시물 이미지 불러오기(테스트 순서: 3)", description = "특정 게시물의 이미지 확인" )
    @GetMapping("/community/image/{fileName}")
    public ResponseEntity<Resource> communityImage(
            @Parameter( description = "게시물 생성 후, 반환된 imagename을 넣어주세요.", example = "e80253ce-1c81-482b-8fb8-f04b7d8117df_.jpg")
            @PathVariable String fileName) {

        ResponseFileDTO result = localFileService.fileFind(fileName);

        // 이미지 타입 및 데이터만 반환
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(result.getContentType()))
                .body(result.getResource());
    }

    @Operation( summary = "게시물 수정하기(테스트 순서: 4)", description = "특정 게시물 데이터 수정" )
    @PutMapping(value = "/community/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ResponseFindBoardDTO>> updateCommunity(
            @RequestPart(name = "to") RequestModifyDTO to, // 데이터 전송 (JSON 형식)
            @RequestPart(name = "file", required = false) MultipartFile file) // 파일 업로드 처리
    {

        ResponseFindBoardDTO result = communityService.modifyBoard(to, file);

        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
