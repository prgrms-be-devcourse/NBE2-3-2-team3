package com.example.bestme.controller;

import com.example.bestme.domain.Result;
import com.example.bestme.dto.ResultRequest;
import com.example.bestme.dto.ResultResponse;
import com.example.bestme.dto.response.ItemDetailResponse;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.ResultService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ResultController {
    private final ResultService resultService;
    private final ModelMapper modelMapper = new ModelMapper();

    @PostMapping("/results/{userId}/{colorId}")
    public ResponseEntity<ApiResponse<ResultResponse.CreateResultResponseDTO>> createResult(@PathVariable Long userId, @PathVariable Long colorId, @RequestBody ResultRequest.CreateResultDTO createResultDTO) {
        Result result = resultService.createResult(userId, colorId, createResultDTO);
        ResultResponse.CreateResultResponseDTO dto = modelMapper.map(result, ResultResponse.CreateResultResponseDTO.class);
        return ResponseEntity.ok(ApiResponse.success("result 생성 완료", dto));
    }

    @GetMapping("/results/{userId}")
    public ResponseEntity<ApiResponse<List<ResultResponse.ReadResultResponseDTO>>> readResults(Long userId) {
        List<Result> results = resultService.readResults(userId);

        ModelMapper modelMapper = new ModelMapper();
        List<ResultResponse.ReadResultResponseDTO> lists = results.stream()
                .map(p -> modelMapper.map(p, ResultResponse.ReadResultResponseDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("전체 result 조회", lists));
    }

    @GetMapping("/results/color/{userId}")
    public ResponseEntity<ApiResponse<ResultResponse.ReadColorResponseDTO>> readColorId(@PathVariable Long userId) {
        ResultResponse.ReadColorResponseDTO result = resultService.readColorId(userId);

        return ResponseEntity.ok(ApiResponse.success("가장 많이 나온 퍼스널 컬러 조회", result));
    }
}
