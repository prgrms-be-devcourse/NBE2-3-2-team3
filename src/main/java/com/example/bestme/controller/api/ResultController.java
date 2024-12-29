package com.example.bestme.controller.api;

import com.example.bestme.domain.Result;
import com.example.bestme.dto.api.ResultRequest;
import com.example.bestme.dto.api.ResultResponse;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.ResultService;
import com.example.bestme.util.jwt.JwtAuthenticationFilter;
import com.example.bestme.util.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Result", description = "퍼스널컬러 진단 관련 API")
public class ResultController {
    private final ResultService resultService;
    private final ChatGPTController chatGPTController;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper = new ModelMapper();

    @PostMapping("/results")
    @Operation( summary = "퍼스널컬러 진단 API", description = "필드에 맞는 색상코드를 전부 입력해주세요." )
    public ResponseEntity<ApiResponse<ResultResponse.CreateResultResponseDTO>> createResult(HttpServletRequest request, @RequestBody ResultRequest.CreateResultDTO createResultDTO) {
        //user의 ID 가져오기 --->
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);
        String accessToken = jwtAuthenticationFilter.resolveToken(request);
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        Long userId = Long.valueOf(claims.getId());
        //<---
        //Chat GPT API 내부적으로 호출
        String requestMessage = "제 피부 톤은 가장 밝은 부분이 #" + createResultDTO.getLightestSkinColor() + ", 가장 어두운 부분이 #" + createResultDTO.getDarkestSkinColor() + "입니다. " +
                "입술 본연의 색상은 #" + createResultDTO.getLipColor() + "입니다. 제 자연 머리카락 색상은 #" + createResultDTO.getHairColor() + "이며, " +
                "눈동자의 기본 색상은 #" + createResultDTO.getPupilColor() + ", 홍채 외곽은 #" + createResultDTO.getIrisColor() + "입니다. " +
                "종합적으로 봤을 때 저에게 가장 가까운 퍼스널 컬러는 무엇일까요?" +
                "봄웜톤이라면 응답으로 1, 여름쿨톤이라면 응답으로 2, 가을웜톤이라면 응답으로 3, 겨울쿨톤이라면 응답으로 4를 주시고" +
                "주어진 답변으로 응답이 어렵다면 5를 주세요. 그렇지만 최대한 5를 내지 않도록 해주세요." +
                "응답 형식 예시: 번호 - 이유(3줄 정도 완성된 문장으로, 문장이 끊기지 않도록 종결어미를 포함해주세요)";
        System.out.println(chatGPTController.chat(requestMessage));
        Long colorId = null;
        if (chatGPTController.chat(requestMessage).startsWith("1")) { colorId = 1L;} else if (chatGPTController.chat(requestMessage).startsWith("2")) {colorId = 2L;
        } else if (chatGPTController.chat(requestMessage).startsWith("3")) { colorId = 3L; } else if (chatGPTController.chat(requestMessage).startsWith("4")) { colorId = 4L;
        } else { colorId = 5L; }
        Result result = resultService.createResult(userId, colorId, createResultDTO);
        ResultResponse.CreateResultResponseDTO dto = modelMapper.map(result, ResultResponse.CreateResultResponseDTO.class);
        return ResponseEntity.ok(ApiResponse.success("result 생성 완료", dto));
    }

    @GetMapping("/results")
    @Operation( summary = "진단 결과 전체 조회 API", description = "한 회원이 진단한 퍼스널컬러의 진단 결과 전체를 조회하는 API입니다." )
    public ResponseEntity<ApiResponse<List<ResultResponse.ReadResultResponseDTO>>> readResults(HttpServletRequest request) {

        //user의 ID 가져오기 --->
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);
        String accessToken = jwtAuthenticationFilter.resolveToken(request);
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        Long userId = Long.valueOf(claims.getId());
        //<---

        List<Result> results = resultService.readResults(userId);

        ModelMapper modelMapper = new ModelMapper();
        List<ResultResponse.ReadResultResponseDTO> lists = results.stream()
                .map(p -> modelMapper.map(p, ResultResponse.ReadResultResponseDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("전체 result 조회", lists));
    }

    @GetMapping("/results/color")
    @Operation( summary = "퍼스널 컬러 조회(가장 높은 빈도수) API", description = "한 회원이 진단했던 기록 중 가장 빈도수가 높게 진단된 퍼스널컬러가 뭔지 진단하는 API입니다." )
    public ResponseEntity<ApiResponse<ResultResponse.ReadColorResponseDTO>> readColorId(HttpServletRequest request) {
        //user의 ID 가져오기 --->
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);
        String accessToken = jwtAuthenticationFilter.resolveToken(request);
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        Long userId = Long.valueOf(claims.getId());
        //<---
        ResultResponse.ReadColorResponseDTO result = resultService.readColorId(userId);

        return ResponseEntity.ok(ApiResponse.success("가장 많이 나온 퍼스널 컬러 조회", result));
    }
}
