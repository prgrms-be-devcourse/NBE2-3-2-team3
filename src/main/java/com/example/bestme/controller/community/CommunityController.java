package com.example.bestme.controller.community;

import com.example.bestme.domain.user.Gender;
import com.example.bestme.domain.user.User;
import com.example.bestme.dto.community.*;
import com.example.bestme.dto.user.RequestSignUpDTO;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.community.CommunityService;
import com.example.bestme.service.community.LocalFileService;
import com.example.bestme.util.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
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

import javax.imageio.IIOException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Tag(name = "Community", description = "커뮤니티 관련 API")
@RequiredArgsConstructor        // final 필드 또는 @NonNull로 지정된 필드만을 매개변수로 받는 생성자를 자동으로 생성
@RestController
@RequestMapping("/api")
public class CommunityController {

    private final CommunityService communityService;
    private final PagedResourcesAssembler<ResponseAllDTO> assembler;
    private final LocalFileService localFileService;
    private final JwtTokenProvider jwtTokenProvider;

    // user_id 추출 메서드 ( request -> 엑세스 토큰 -> user_id )
    public Long getUserId(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        Claims claims = jwtTokenProvider.parseClaims(token);
        Long userId = Long.valueOf(claims.getId());
        return userId;
    }

    // 페이지 API의 응답 DTO 세팅 메서드 ( + 링크 생성 )
    public PagedModel<EntityModel<ResponseAllDTO>> responsePageDtoSetting( Page<ResponseAllDTO> results, RequestPageDTO pageDTO, String url ) {

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
        Link selfLink = Link.of(url + originPage).withSelfRel();
        // 'prev' 링크가 첫 페이지를 넘지 않게 처리
        Link prevLink = null;
        if( !results.isFirst() ) prevLink = Link.of(url + (originPage - 1) ).withRel("prevPage");
        // 'next' 링크는 마지막 페이지를 넘지 않게 처리
        Link nextLink = null;
        if( !results.isLast() ) nextLink = Link.of(url + (originPage + 1)).withRel("nextPage");
        // 이전 페이지 그룹의 첫 페이지 링크
        Link firstLink = Link.of(url + "1").withRel("firstPageOfPrevGroup");
        // 현재 페이지 그룹의 첫 페이지 번호가 1이 아닐 경우 -> 이전 페이지 그룹의 첫 페이지로 이동
        if( startPage > 1 ) firstLink = Link.of(url + (startPage - numberOfPagesPerGroup) ).withRel("firstPageOfPrevGroup");
        // 이후 페이지 그룹의 첫 페이지 링크
        Link lastLink = Link.of(url + results.getTotalPages()).withRel("lastPageOfNextGroup");
        // 현재 페이지 그룹의 마지막 페이지 번호가 총 페이지 갯수가 아닐 경우 -> 다음 페이지 그룹의 첫 페이지로 이동
        if( lastPage < results.getTotalPages() ) lastLink = Link.of(url + (startPage + numberOfPagesPerGroup)).withRel("lastPageOfNextGroup");

        // 기존 링크를 수정된 링크로 교체
        if( prevLink != null ) pagedModel.add(prevLink);
        pagedModel.add(selfLink);
        if( nextLink != null ) pagedModel.add(nextLink);
        pagedModel.add(firstLink);
        pagedModel.add(lastLink);

        return pagedModel;
    }

    // 테스트용 게시물 55개 생성 API
    @Operation( summary = "테스트용 게시물 생성(테스트 끝나면 upload 폴더에 있는 이미지 삭제하세요!)"
            , description = "55개의 게시물이 자동 생성(55번 게시물만 이미지 추가)" )
    @PostMapping( "/community/createTestBoard")
    public ResponseEntity<ApiResponse<RequestSignUpDTO>> testCreate() {

        // 테스트용 사용자 생성 ( 관리자 )
        RequestSignUpDTO joinDTO = new RequestSignUpDTO();
        joinDTO.setEmail("bestme@gmail.com");
        joinDTO.setPassword("bestme12!");
        joinDTO.setNickname("관리자");
        joinDTO.setBirth("960617");
        joinDTO.setGender(Gender.M);

        User user = communityService.createTestUser(joinDTO);

        int count = 0;
        for( int i=1; i < 56; i++) {
            RequestWriteDTO to = new RequestWriteDTO();
            to.setUserId(user.getId());
            to.setUser(user);
            Path sourcePath = null;
            if( i == 55 ) {
                to.setSubject("<공지> 업로드한 이미지 미리보기");
                to.setContent("현재 이미지는 1개만 업로드가 가능하고 글 위에 표시됩니다.");
                sourcePath = Paths.get("src/main/resources/static/imgs/community/coordi_image1.jpg");
            } else {
                to.setSubject("테스트용 게시물 " + i);
                to.setContent("테스트용 내용 " + i);
            }
            communityService.createTestBoard(to, sourcePath);
            count++;
        }

        return ResponseEntity.ok(ApiResponse.success("테스트용 게시물 55개 생성 완료", joinDTO));
    }

    // 게시물 Create API
    @Operation( summary = "게시물 Create API"
            , description = "지정된 카테고리가 없을 경우 필드를 제거해주세요.<br> userId, user, imagename 필드는 지워주세요." )
    @PostMapping(value = "/community/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ResponseFindDTO>> communityWrite(
            @RequestPart(name = "to") RequestWriteDTO to, // 데이터 전송 (JSON 형식)
            @RequestPart(name = "file", required = false) MultipartFile file, // 파일 업로드 처리
            HttpServletRequest request
    ) {
        // 요청 내 토큰에서 userId 추출
        Long userId = getUserId(request);
        to.setUserId(userId);
        ResponseFindDTO result = communityService.createBoard(to, file);

        return ResponseEntity.ok(ApiResponse.success("게시물 생성 완료", result));
    }

    // 게시물 목록 Read API ( 페이징 기능 추가 )
    @Operation( summary = "게시물 목록 Read API", description = "세션에 있는 페이지 번호 사용(기본 페이지 번호 : 1)" )
    @GetMapping( "/community" )
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<ResponseAllDTO>>>> findAllBoard(
            @Parameter(description = "페이지 번호", example = "1")
            HttpSession session)
    {

        // 세션에서 현재 페이지 번호 가져오기
        Integer page = (Integer) session.getAttribute("currentPageNumber");
        if (page == null) page = 1; // 세션에 page 데이터가 없을 경우 (기본값 설정)

        // page 요청 DTO 객체 생성
        RequestPageDTO pageDTO = new RequestPageDTO(page);
        int currentPage = pageDTO.getCurrentPage();
        int numberOfDataPerPage = pageDTO.getNumberOfDataPerPage();
        Page<ResponseAllDTO> results = communityService.findAllBoard(currentPage, numberOfDataPerPage);

        // Page 응답용에 첨부할 url 생성
        String url = "/community/";

        // Page 응답용 DTO로 변환 및 생성
        PagedModel<EntityModel<ResponseAllDTO>> pagedModel = responsePageDtoSetting(results, pageDTO, url);

        // ApiResponse에 PagedModel을 넣어서 반환
        return ResponseEntity.ok(ApiResponse.success(pagedModel));
    }

    // 지정 게시물 Read API
    @Operation( summary = "게시물 Read API", description = "지정 게시물의 세부 내용 확인 ( 이미지 파일 제외 )" )
    @GetMapping( "/community/detail/{boardId}" )
    public ResponseEntity<ApiResponse<ResponseFindDTO>> communityDetail(@PathVariable String boardId) {
        ResponseFindDTO result = communityService.findBoardById(boardId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 지정 게시물 이미지 파일 Read API (바이너리 타입으로 반환 - 스웨거에 사용하려고 만듬 )
    @Operation( summary = "단일 이미지 파일 Read API", description = "지정 게시물의 이미지 확인" )
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

    // 지정 게시물 이미지 파일 Read API (url 타입으로 반환)
    /*
    @GetMapping("/community/image/{fileName}")
    public ResponseEntity<String> communityImage(
            @Parameter(description = "게시물 생성 후, 반환된 imagename을 넣어주세요.", example = "e80253ce-1c81-482b-8fb8-f04b7d8117df_.jpg")
            @PathVariable String fileName) {

        // 이미지 파일 경로 및 URL 생성
        String imageUrl = getImageUrl(fileName);

        // 이미지 URL을 반환
        return ResponseEntity.ok(imageUrl);
    }
     */

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
    @Operation( summary = "게시물 Modify API", description = "지정 게시물 수정" )
    @PutMapping(value = "/community/modify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ResponseFindDTO>> updateCommunity(
            @RequestPart(name = "to") RequestModifyDTO to,                           // 데이터 전송 (JSON 형식)
            @RequestPart(name = "file", required = false) MultipartFile file,        // 파일 업로드 처리
            HttpServletRequest request
    ) {
        // 요청 내 토큰에서 userId 추출
        Long userId = getUserId(request);
        to.setUserId(userId);
        ResponseFindDTO result = communityService.modifyBoard(to, file);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 지정 게시물 Delete API
    @Operation( summary = "게시물 Delete API", description = "지정 게시물 삭제" )
    @DeleteMapping(value = "/community/delete")
    public ResponseEntity<ApiResponse<ResponseDeleteDTO>> deleteCommunity(
            @RequestBody RequestDeleteDTO to,                       // 스웨거용 ( application/json )
//            @ModelAttribute RequestDeleteDTO to,                  // HTML용 ( multipart/form-data )
            HttpServletRequest request
    ) {
        // 요청 내 토큰에서 userId 추출
        Long userId = getUserId(request);
        to.setUserId(userId);
        ResponseDeleteDTO result = communityService.deleteBoard(to);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 현재 User가 작성한 게시물 목록 API
    @Operation( summary = "현재 사용자 게시물 목록 Read API", description = "세션에 있는 페이지 번호 사용(기본 페이지 번호 : 1)" )
    @GetMapping( "/member/my_boards" )
    public ResponseEntity<ApiResponse<PagedModel<EntityModel<ResponseAllDTO>>>> findAllBoardByUserId(
            @Parameter(description = "페이지 번호", example = "1")
            HttpSession session,
            HttpServletRequest request)
    {
        // 세션에서 현재 페이지 번호 가져오기
        Integer page = (Integer) session.getAttribute("userPageNumber");
        if (page == null) page = 1; // 세션에 page 데이터가 없을 경우 (기본값 설정)
        // 요청 내 토큰에서 userId 추출
        Long userId = getUserId(request);

        // page 요청 DTO 객체 생성
        RequestPageDTO pageDTO = new RequestPageDTO(page);
        int currentPage = pageDTO.getCurrentPage();
        int numberOfDataPerPage = pageDTO.getNumberOfDataPerPage();
        Page<ResponseAllDTO> results = communityService.findAllBoardByUserId(currentPage, numberOfDataPerPage, userId);

        // Page 응답용에 첨부할 url 생성
        String url = "/member/my_posting/";

        // Page 응답용 DTO로 변환 및 생성
        PagedModel<EntityModel<ResponseAllDTO>> pagedModel = responsePageDtoSetting(results, pageDTO, url);

        // ApiResponse에 PagedModel을 넣어서 반환
        return ResponseEntity.ok(ApiResponse.success(pagedModel));
    }
}
