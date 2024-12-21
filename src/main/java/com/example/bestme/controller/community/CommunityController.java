package com.example.bestme.controller.community;

import com.example.bestme.domain.community.Community;
import com.example.bestme.dto.community.ResponseAllBoardDTO;
import com.example.bestme.dto.community.RequestWriteDTO;
import com.example.bestme.dto.community.ResponseFindBoardDTO;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.community.CommunityService;
import com.example.bestme.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Community", description = "커뮤니티 관련 API")
@RestController
@RequestMapping("/api")
public class CommunityController {

    private final CommunityService communityService;
    private final PagedResourcesAssembler<ResponseAllBoardDTO> assembler;
    Integer currentPage;
    Integer numberOfDataPerPage = 5;      // 페이지당 데이터 갯수
    Integer numberOfPagesPerGroup = 5;    // 페이지 그룹당 페이지 갯수
//    Integer numberPageGroup;              // 페이지 그룹 갯수
    Integer startpageOfGroup;             // 현재 페이지 그룹의 첫 페이지 번호
    Integer lastPageOfGroup;              // 현재 페이지 그룹의 마지막 페이지 번호

    public CommunityController(CommunityService communityService, PagedResourcesAssembler<ResponseAllBoardDTO> assembler) {
        this.communityService = communityService;
        this.assembler = assembler;
    }

    @Operation( summary = "테스트용 게시물 생성"
            , description = "55개의 게시물이 자동 생성됩니다" )
    @PostMapping( "/community/testCreate")
    public ResponseEntity<ApiResponse<String>> testCreate(
    ) {
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

    // 게시물 목록 응답 메서드 ( 페이징 기능 추가 )
    @Operation( summary = "게시물 목록"
            , description = "시작 페이지 번호 1, " )
    @GetMapping( "/community/{page}" )
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<ResponseAllBoardDTO>>>> findAllBoard(
            @Parameter(description = "페이지 번호", example = "1")
            @PathVariable Integer page)
    {
        // 클라이언트가 보낸 페이지 번호는 1부터 시작하므로, 0 기반 인덱스로 변환
        this.currentPage = page - 1;
        Page<ResponseAllBoardDTO> results = communityService.findAllBoard(currentPage, numberOfDataPerPage);

        // PagedModel을 사용하여 페이지화된 데이터 래핑
        PagedModel<EntityModel<ResponseAllBoardDTO>> pagedModel = assembler
                .toModel(results, result
                        -> EntityModel.of(result, Link.of("/community/detail/" + result.getBoardId())));

        // 기존적으로 자동 생성되는 링크 제거
        pagedModel.removeLinks();

        // 페이지 그룹 갯수 산출
//        int totalPages = results.getTotalPages();
//        this.numberPageGroup = ( totalPages + ( numberOfPagesPerGroup - 1 ) ) / numberOfPagesPerGroup;

        // 현재 페이지에 따라 링크 수정
        Link selfLink = Link.of("/community/" + page).withSelfRel();

        // 'next' 링크는 마지막 페이지를 넘지 않게 처리
        Link nextLink = null;
        if( !results.isLast() ) nextLink = Link.of("/community/" + (page + 1)).withRel("nextPage");
        // 'prev' 링크가 첫 페이지를 넘지 않게 처리
        Link prevLink = null;
        if( !results.isFirst() ) prevLink = Link.of("/community/" + (page - 1) ).withRel("prevPage");

        // 현재 페이지 그룹의 첫 번째 페이지 번호
        this.startpageOfGroup = (currentPage - ( currentPage - 1 ) % numberOfPagesPerGroup);
        // 현재 페이지 그룹의 마지막 페이지 번호
        this.lastPageOfGroup = (currentPage - ( currentPage - 1 ) % numberOfPagesPerGroup + numberOfPagesPerGroup - 1);

        // 이전 페이지 그룹의 첫 페이지 링크
        Link firstLink = Link.of("/community/1").withRel("firstPageOfPrevGroup");
        if( startpageOfGroup != 1 ) {
            // 현재 페이지 그룹의 첫 페이지 번호가 1이 아닐 경우 -> 이전 페이지 그룹의 첫 페이지로 이동
            firstLink = Link.of("/community/" + (startpageOfGroup - numberOfPagesPerGroup) ).withRel("firstPageOfPrevGroup");
        }

        // 이후 페이지 그룹의 첫 페이지 링크
        Link lastLink = Link.of("/community/" + results.getTotalPages()).withRel("lastPageOfNextGroup");
        if( lastPageOfGroup != results.getTotalPages() ) {
            // 현재 페이지 그룹의 마지막 페이지 번호가 총 페이지 갯수가 아닐 경우 -> 다음 페이지 그룹의 첫 페이지로 이동
            lastLink = Link.of("/community/" + (startpageOfGroup + numberOfPagesPerGroup)).withRel("lastPageOfNextGroup");
        }

        // 기존 링크를 수정된 링크로 교체
        pagedModel.add(selfLink);
        if( nextLink != null ) pagedModel.add(nextLink);
        if( prevLink != null ) pagedModel.add(prevLink);
        pagedModel.add(firstLink);
        pagedModel.add(lastLink);

        // ApiResponse에 PagedModel을 넣어서 반환
        return ResponseEntity.ok(ApiResponse.success(pagedModel));
    }

}
