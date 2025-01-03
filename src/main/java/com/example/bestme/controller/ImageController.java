package com.example.bestme.controller;

import com.example.bestme.service.LocalImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
@Tag(name = "Image", description = "저장된 이미지를 화면에 출력하는 API")
public class ImageController {

    private final LocalImageService imageService;

    @GetMapping("/items/{itemId}")
    @Operation( summary = "아이템 이미지 출력", description = "upload/item-img 디렉토리의 이미지 출력" )
    public ResponseEntity<Resource> downloadItemImage(@PathVariable Long itemId) throws MalformedURLException {
        Resource response = imageService.getItemImageResource(itemId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(response);
    }

    @GetMapping("/brands/{brandId}")
    @Operation( summary = "브랜드 이미지 출력", description = "upload/brand-img 디렉토리의 이미지 출력" )
    public ResponseEntity<Resource> downloadBrandImage(@PathVariable Long brandId) throws MalformedURLException {
        Resource response = imageService.getBrandImageResource(brandId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(response);
    }
}
