package com.example.bestme.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Set;
import java.util.UUID;

@Service
public class LocalImageService implements ImageService {

    private static final Set<String> ALLOW_IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png");

    @Value("${image.dir}")
    private String imageDir;

    @Override
    public String save(MultipartFile image, String targetDir) {
        try {
            String storeImageName = createStoreImageName(image);
            Path fullPath = Paths.get(imageDir, targetDir)
                    .resolve(storeImageName)
                    .toAbsolutePath();
            image.transferTo(new File(fullPath.toString()));
            return fullPath.toUri().toURL().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createStoreImageName(MultipartFile image) {
        String originalImageName = getOriginalImageName(image);
        String extension = extractExtension(originalImageName);
        return MessageFormat.format("{0}.{1}", UUID.randomUUID().toString(), extension);
    }

    private String getOriginalImageName(MultipartFile image) {
        String originalImageName = image.getOriginalFilename();
        if (originalImageName == null || !originalImageName.contains(".")) {
            throw new IllegalArgumentException("잘못된 이미지");
        }
        return originalImageName;
    }

    private String extractExtension(String originalImageName) {
        String extension = originalImageName.substring(originalImageName.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOW_IMAGE_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("잘못된 이미지 확장자");
        }
        return extension;
    }
}
