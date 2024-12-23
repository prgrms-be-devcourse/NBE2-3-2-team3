package com.example.bestme.controller;

import com.example.bestme.service.LocalImageService;
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
public class ImageController {

    private final LocalImageService imageService;

    @GetMapping("/items/{itemId}")
    public ResponseEntity<Resource> downloadItemImage(@PathVariable Long itemId) throws MalformedURLException {
        Resource response = imageService.getItemImageResource(itemId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(response);
    }

    @GetMapping("/brands/{brandId}")
    public ResponseEntity<Resource> downloadBrandImage(@PathVariable Long brandId) throws MalformedURLException {
        Resource response = imageService.getBrandImageResource(brandId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(response);
    }
}
