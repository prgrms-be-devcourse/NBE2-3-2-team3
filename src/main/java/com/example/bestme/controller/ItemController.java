package com.example.bestme.controller;

import com.example.bestme.dto.request.SearchConditionRequest;
import com.example.bestme.dto.response.CategoryMenuResponse;
import com.example.bestme.dto.response.FilterMenuResponse;
import com.example.bestme.dto.response.ItemDetailResponse;
import com.example.bestme.dto.response.ItemsResponse;
import com.example.bestme.exception.ApiResponse;
import com.example.bestme.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ApiResponse<ItemsResponse> getItems(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) List<String> brands,
            @RequestParam(required = false) List<String> colors
    ) {
        SearchConditionRequest searchConditionRequest = SearchConditionRequest.of(categoryId, brands, colors);
        ItemsResponse response = itemService.getItemsResponseBySearchCondition(searchConditionRequest);
        return ApiResponse.success(response);
    }

    @GetMapping("/{itemId}")
    public ApiResponse<ItemDetailResponse> getItem(@PathVariable Long itemId) {
        ItemDetailResponse response = itemService.getItemDetailResponse(itemId);
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