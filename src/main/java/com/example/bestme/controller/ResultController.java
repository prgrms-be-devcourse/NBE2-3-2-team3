package com.example.bestme.controller;

import com.example.bestme.domain.Result;
import com.example.bestme.dto.ResultRequest;
import com.example.bestme.dto.ResultResponse;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.ResultService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
