package com.example.bestme.controller.community;

import com.example.bestme.dto.community.*;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.community.CommunityService;
import com.example.bestme.service.community.LocalFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.Authenticator;

@Tag(name = "Community", description = "커뮤니티 관련 API")
@RequiredArgsConstructor        // final 필드 또는 @NonNull로 지정된 필드만을 매개변수로 받는 생성자를 자동으로 생성
@RestController
@RequestMapping("/api")
public class CommunityController {

    private final CommunityService communityService;
    private final PagedResourcesAssembler<ResponseAllDTO> assembler;
    private final LocalFileService localFileService;

    // 생성자 ( 사용 x )
    /*
    public CommunityController(CommunityService communityService, PagedResourcesAssembler<ResponseAllDTO> assembler
            , LocalFileService localFileService) {
        this.communityService = communityService;
        this.assembler = assembler;
        this.localFileService = localFileService;
    }
     */

    @Operation( summary = "테스트용 게시물 생성(테스트 순서: 1)", description = "55개의 게시물이 자동 생성" )
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

    // 게시물 Create API
    @Operation( summary = "게시물 Create API(테스트 순서: 1)"
            , description = "지정된 카테고리가 없을 경우 필드를 제거해주세요.<br> id와 imagename 필드는 지워주세요." )
    @PostMapping(value = "/community/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ResponseFindDTO>> communityWrite(
            @RequestPart(name = "to") RequestWriteDTO to, // 데이터 전송 (JSON 형식)
            @RequestPart(name = "file", required = false) MultipartFile file, // 파일 업로드 처리
            Authentication authentication       // 인증객체 :  UserServiceImpl - createToken에서 만든 토큰 가져오기
    ) {

        to.setUserId(Long.valueOf(authentication.getName()));       // 인증객체에 있는 user_id를 DTO에 삽입
        ResponseFindDTO result = communityService.createBoard(to, file);

        return ResponseEntity.ok(ApiResponse.success("게시물 생성 완료", result));
    }

    // 게시물 목록 Read API ( 페이징 기능 추가 )
    @Operation( summary = "게시물 목록 Read API(테스트 순서: 2)", description = "시작 페이지 번호 1" )
    @GetMapping( "/community" )
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<ResponseAllDTO>>>> findAllBoard(
            @Parameter(description = "페이지 번호", example = "1")
            HttpSession session)
    {
        // 세션에서 현재 페이지 번호 가져오기
        Integer page = (Integer) session.getAttribute("currentPage");
        if (page == null) page = 1; // 세션에 page 데이터가 없을 경우 (기본값 설정)

        // page 요청 DTO 객체 생성
        RequestPageDTO pageDTO = new RequestPageDTO(page);
        int currentPage = pageDTO.getCurrentPage();
        int numberOfDataPerPage = pageDTO.getNumberOfDataPerPage();
        Page<ResponseAllDTO> results = communityService.findAllBoard(currentPage, numberOfDataPerPage);

        // PagedModel을 사용하여 페이지화된 데이터 래핑
        PagedModel<EntityModel<ResponseAllDTO>> pagedModel = assembler
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

    // 지정 게시물 Read API
    @Operation( summary = "게시물 Read API(테스트 순서: 2)", description = "지정 게시물의 세부 내용 확인 ( 이미지 파일 제외 )" )
    @GetMapping( "/community/detail/{boardId}" )
    public ResponseEntity<ApiResponse<ResponseFindDTO>> communityDetail(@PathVariable String boardId) {
        ResponseFindDTO result = communityService.findBoardById(boardId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 지정 게시물 이미지 파일 Read API
    @Operation( summary = "단일 이미지 파일 Read API(테스트 순서: 3)", description = "지정 게시물의 이미지 확인" )
    @GetMapping("/community/image/{fileName}")
    public ResponseEntity<Resource> communityImage(
            @Parameter( description = "게시물 생성 후, 반환된 imagename을 넣어주세요.", example = "e80253ce-1c81-482b-8fb8-f04b7d8117df_.jpg")
            @PathVariable String fileName)
    {
        ResponseFileDTO result = localFileService.fileFind(fileName);
        // 이미지 타입 및 데이터만 반환
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(result.getContentType()))
                .body(result.getResource());
    }

    // 지정 게시물 Navigation API ( 어려워서 구현 보류 )
    /*
    @Operation( summary = "지정 게시물 Navigation API(테스트 순서: 3)", description = "지정 게시물의 이전글, 다음글 데이터(제목, 링크) 반환" )
    @GetMapping( "/community/detail/{boardId}/navigation" )
    public ResponseEntity<ApiResponse<List<ResponseNavigationDTO>>> findBoardNavigation(@PathVariable Long boardId) {

        List<ResponseNavigationDTO> result = communityService.boardNavigation(boardId);

        result.setPrevBoardLink( result.getPrevBoardId() != null ? Link.of("/community/" + result.getPrevBoardId()) : null );
        result.setNextBoardLink( result.getNextBoardId() != null ? Link.of("/community/" + result.getNextBoardId()) : null );

        return ResponseEntity.ok(ApiResponse.success(result));
    }
     */

    // 지정 게시물 Modify API
    @Operation( summary = "게시물 Modify API (테스트 순서: 4)", description = "지정 게시물 수정" )
    @PutMapping(value = "/community/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ResponseFindDTO>> updateCommunity(
            @RequestPart(name = "to") RequestModifyDTO to,                           // 데이터 전송 (JSON 형식)
            @RequestPart(name = "file", required = false) MultipartFile file,        // 파일 업로드 처리
            Authentication authentication                                            // 인증객체 :  UserServiceImpl - createToken에서 만든 토큰 가져오기
    ) {

        to.setUserId(Long.valueOf(authentication.getName()));               // 인증객체에 있는 user_id를 DTO에 삽입
        ResponseFindDTO result = communityService.modifyBoard(to, file);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation( summary = "게시물 Delete API (테스트 순서: 5)", description = "지정 게시물 삭제" )
    @DeleteMapping(value = "/community/delete")
    public ResponseEntity<ApiResponse<ResponseDeleteDTO>> deleteCommunity(
            @RequestBody RequestDeleteDTO to,                       // 스웨거용 ( application/json )
//            @ModelAttribute RequestDeleteDTO to,                  // HTML용 ( multipart/form-data )
            Authentication authentication                           // 인증객체 :  UserServiceImpl - createToken에서 만든 토큰 가져오기
    ) {

        to.setUserId(Long.valueOf(authentication.getName()));       // 인증객체에 있는 user_id를 DTO에 삽입
        ResponseDeleteDTO result = communityService.deleteBoard(to);

        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
