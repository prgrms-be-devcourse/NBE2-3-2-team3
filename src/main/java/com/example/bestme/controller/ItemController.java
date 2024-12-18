package com.example.bestme.controller;

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

//    @GetMapping
//    public ApiResponse<ItemsResponse> getItemsV1(
//            @RequestParam(required = false) List<String> categories,
//            @RequestParam(required = false) List<String> colors
//    ) {
//        FilterRequest filterRequest = FilterRequest.of(categories, colors);
//        ItemsResponse response = itemService.getItemsResponseByFilterV1(filterRequest);
//        return ApiResponse.success(response);
//    }

    @GetMapping
    public ApiResponse<ItemsResponse> getItemsV2(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) List<String> colors
    ) {
        ItemsResponse response = itemService.getItemsResponseByFilterV2(categoryId, colors);
        return ApiResponse.success(response);
    }

    @GetMapping("/{id}")
    public ApiResponse<ItemDetailResponse> getItem(@PathVariable Long id) {
        ItemDetailResponse response = itemService.getItemDetailResponse(id);
        return ApiResponse.success(response);
    }

    @GetMapping("/filter-menu")
    public ApiResponse<FilterMenuResponse> getFilterMenu() {
        FilterMenuResponse response = itemService.getFilterMenuResponse();
        return ApiResponse.success(response);
    }
}