package com.example.bestme.controller;

import com.example.bestme.dto.response.ItemDetailResponse;
import com.example.bestme.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ItemService itemService;

    @GetMapping("/items/{itemId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long itemId) {
        try {
            ItemDetailResponse item = itemService.getItemDetailResponse(itemId);
            Resource response = new UrlResource(item.imageUrl());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
