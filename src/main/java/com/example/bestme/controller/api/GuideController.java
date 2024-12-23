package com.example.bestme.controller.api;

import com.example.bestme.domain.Guide;
import com.example.bestme.dto.api.GuideResponse;
import com.example.bestme.dto.api.ResultResponse;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.GuideService;
import com.example.bestme.service.ResultService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/guide/{userId}")
    public ResponseEntity<ApiResponse<List<GuideResponse.ReadGuideResponseDTO>>> readGuides(@PathVariable Long userId) {

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
    public ResponseEntity<ApiResponse<GuideResponse.ReadGuideResponseDTO>> createGuide() {




        return ResponseEntity.ok(ApiResponse.success("사용자에게 가장 높은 빈도수의 퍼스널 컬러에 대한 가이드 조회", lists));
    }
}
