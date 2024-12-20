package com.example.bestme.service.community;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String fileUpload(MultipartFile file) throws Exception;
    void fileDelete(String filename);
}
