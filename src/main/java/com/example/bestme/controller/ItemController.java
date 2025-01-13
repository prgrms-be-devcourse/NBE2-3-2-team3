package com.example.bestme.controller;

import com.example.bestme.dto.request.SearchConditionRequest;
import com.example.bestme.dto.response.*;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.ItemService;
import com.example.bestme.service.LikeService;
import com.example.bestme.util.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static com.example.bestme.util.StringUtils.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
@Tag(name = "Item", description = "아이템 페이지와 추천 아이템 페이지에서 아이템과 관련된 작업을 위한 API")
public class ItemController {
    
    private final ItemService itemService;
    private final LikeService likeService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    @Operation( summary = "아이템 찾기", description = "한 페이지에 12개의 아이템으로 페이징된 아이템을 찾는 API" )
    public ApiResponse<ItemsResponse> getItems(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String brands,
            @RequestParam(required = false) String colors,
            @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        SearchConditionRequest searchConditionRequest = SearchConditionRequest.of(
                categoryId,
                convertStringToList(brands),
                convertStringToList(colors)
        );
        ItemsResponse response = itemService.getSliceItemsResponseBySearchCondition(searchConditionRequest, pageable);
        return ApiResponse.success(response);
    }

    @GetMapping("/{itemId}")
    @Operation( summary = "아이템 상세 정보", description = "아이템의 상세 정보를 찾는 API" )
    public ApiResponse<ItemDetailResponse> getItem(@PathVariable Long itemId) {
        ItemDetailResponse response = itemService.getItemDetailResponse(itemId);
        return ApiResponse.success(response);
    }

    @PutMapping("/{itemId}/like")
    @Operation( summary = "좋아요 기능", description = "로그인한 사용자가 좋아요 클릭 시 좋아요 수 +1 또는 -1" )
    public ApiResponse<LikeResponse> clickItemLike(
            @PathVariable Long itemId,
            HttpServletRequest request
    ) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        Long userId = Long.valueOf(claims.getId());

        LikeResponse response = likeService.getLikeResponse(itemId, userId);
        return ApiResponse.success(response);
    }

    @GetMapping("/recommend")
    @Operation( summary = "추천 아이템 조회", description = "최상위 카테고리 별로 좋아요 수가 많은 3개 아이템을 추천 아이템으로 선택" )
    public ApiResponse<RecommendItemsResponse> getRecommendItems(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        Long userId = Long.valueOf(claims.getId());

        RecommendItemsResponse response = itemService.getRecommendItemsResponse(userId);
        return ApiResponse.success(response);
    }

    @GetMapping("/categories")
    @Operation( summary = "카테고리 조회", description = "모든 카테고리 조회" )
    public ApiResponse<CategoryMenuResponse> getCategoryMenu() {
        CategoryMenuResponse response = itemService.getCategoryMenuResponse();
        return ApiResponse.success(response);
    }

    @GetMapping("/filters")
    @Operation( summary = "필터 정보 조회", description = "모든 브랜드와 모든 퍼스널컬러 조회" )
    public ApiResponse<FilterMenuResponse> getFilterMenu(
            @RequestParam(required = false) Long categoryId
    ) {
        FilterMenuResponse response = itemService.getFilterMenuResponse(categoryId);
        return ApiResponse.success(response);
    }
}