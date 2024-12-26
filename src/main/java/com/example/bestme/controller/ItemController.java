package com.example.bestme.controller;

import com.example.bestme.dto.request.SearchConditionRequest;
import com.example.bestme.dto.response.*;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.ItemService;
import com.example.bestme.service.LikeService;
import com.example.bestme.util.jwt.JwtAuthenticationFilter;
import com.example.bestme.util.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    
    private final ItemService itemService;
    private final LikeService likeService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ApiResponse<ItemsResponse> getItems(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) List<String> brands,
            @RequestParam(required = false) List<String> colors,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        SearchConditionRequest searchConditionRequest = SearchConditionRequest.of(categoryId, brands, colors);
        ItemsResponse response = itemService.getPagingItemsResponseBySearchCondition(searchConditionRequest, pageable);
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
//            HttpServletRequest request
            @RequestParam Long userId
    ) {
//        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);
//        String accessToken = jwtAuthenticationFilter.resolveToken(request);
//        Claims claims = jwtTokenProvider.parseClaims(accessToken);
//        Long userId = Long.valueOf(claims.getId());

        LikeResponse response = likeService.clickItemLike(itemId, userId);
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