package com.example.bestme.controller;

import com.example.bestme.dto.request.SearchConditionRequest;
import com.example.bestme.dto.response.*;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.ItemService;
import com.example.bestme.service.LikeService;
import com.example.bestme.util.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
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
public class ItemController {
    
    private final ItemService itemService;
    private final LikeService likeService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
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
    public ApiResponse<ItemDetailResponse> getItem(@PathVariable Long itemId) {
        ItemDetailResponse response = itemService.getItemDetailResponse(itemId);
        return ApiResponse.success(response);
    }

    @PutMapping("/{itemId}/like")
    public ApiResponse<LikeResponse> clickItemLike(
            @PathVariable Long itemId,
            HttpServletRequest request
    ) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        Long userId = Long.valueOf(claims.getId());

        LikeResponse response = likeService.clickItemLike(itemId, userId);
        return ApiResponse.success(response);
    }

    @GetMapping("/recommend")
    public ApiResponse<RecommendItemsResponse> getRecommendItems() {
        RecommendItemsResponse response = itemService.getRecommendItemsResponse();
        return ApiResponse.success(response);
    }

    @GetMapping("/categories")
    public ApiResponse<CategoryMenuResponse> getCategoryMenu() {
        CategoryMenuResponse response = itemService.getCategoryMenuResponse();
        return ApiResponse.success(response);
    }

    @GetMapping("/filters")
    public ApiResponse<FilterMenuResponse> getFilterMenu(
            @RequestParam(required = false) Long categoryId
    ) {
        FilterMenuResponse response = itemService.getFilterMenuResponse(categoryId);
        return ApiResponse.success(response);
    }
}