package com.example.bestme.controller;

import com.example.bestme.dto.request.FilterRequest;
import com.example.bestme.dto.response.FilterDataResponse;
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
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) List<String> colors
    ) {
        FilterRequest filterRequest = FilterRequest.of(categories, colors);
        ItemsResponse response = itemService.getItemsResponseByFilter(filterRequest);
        return ApiResponse.success(response);
    }

    @GetMapping("/{id}")
    public ApiResponse<ItemDetailResponse> getItem(@PathVariable Long id) {
        ItemDetailResponse response = itemService.getItemDetailResponse(id);
        return ApiResponse.success(response);
    }

//    @GetMapping("/filters")
//    public ApiResponse<FilterDataResponse> getFilterData() {
//        FilterDataResponse response = itemService.getFilterDataResponse();
//        return ApiResponse.success(response);
//    }
}