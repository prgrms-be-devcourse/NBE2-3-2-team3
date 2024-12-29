package com.example.bestme.controller.api;

import com.example.bestme.domain.Guide;
import com.example.bestme.dto.api.GuideRequest;
import com.example.bestme.dto.api.GuideResponse;
import com.example.bestme.dto.api.ResultResponse;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.GuideService;
import com.example.bestme.service.ResultService;
import com.example.bestme.util.jwt.JwtAuthenticationFilter;
import com.example.bestme.util.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Guide", description = "퍼스널컬러 진단 결과에 따른 가이드 제공 관련 API")
public class GuideController {
    private final GuideService guideService;
    private final ResultService resultService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/guide")
    @Operation( summary = "회원의 퍼스널컬러에 대한 가이드를 전체 조회하는 API입니다.", description = "(모든 카테고리 / 회원)의 스타일 가이드 " )
    public ResponseEntity<ApiResponse<List<GuideResponse.ReadGuideResponseDTO>>> readGuides(HttpServletRequest request) {

        //user의 ID 가져오기 --->
        String accessToken = jwtTokenProvider.resolveToken(request);
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        Long userId = Long.valueOf(claims.getId());
        //<---

        //가장 높은 빈도수의 퍼스널컬러 ID 가져오기
        ResultResponse.ReadColorResponseDTO result = resultService.readColorId(userId);
        Long colorId = result.getColorId();

        List<Guide> results = guideService.readGuides(colorId);

        ModelMapper modelMapper = new ModelMapper();
        List<GuideResponse.ReadGuideResponseDTO> lists = results.stream()
                .map(p -> modelMapper.map(p, GuideResponse.ReadGuideResponseDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("사용자에게 가장 높은 빈도수의 퍼스널 컬러에 대한 가이드 조회", lists));
    }

    @PostMapping("/guide")
    @Operation( summary = "가이드를 등록하는 API입니다.", description = "카테고리ID, 컬러ID, 설명은 필수 항목입니다. " )
    public ResponseEntity<ApiResponse<GuideResponse.CreateGuideResponseDTO>> createGuide(@RequestBody GuideRequest.CreateGuideDTO createGuideDTO) {
        //등록한 카테고리 아이디
        Long categoryId = createGuideDTO.getCategory();

        //가이드 등록
        Guide guide = guideService.createGuide(createGuideDTO);
        GuideResponse.CreateGuideResponseDTO dto = modelMapper.map(guide, GuideResponse.CreateGuideResponseDTO.class);

        return ResponseEntity.ok(ApiResponse.success("가이드 등록에 성공했습니다. (categoryId: " + categoryId + ")", dto));
    }
}
