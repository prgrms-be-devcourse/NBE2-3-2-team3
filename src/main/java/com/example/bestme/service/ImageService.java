package com.example.bestme.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String save(MultipartFile image, String targetDir);
}
