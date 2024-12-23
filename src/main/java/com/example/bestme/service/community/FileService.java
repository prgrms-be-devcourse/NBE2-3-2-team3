package com.example.bestme.service.community;

import com.example.bestme.dto.community.ResponseFileDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String fileUpload(MultipartFile file) throws Exception;
    ResponseFileDTO fileFind(String filename) throws Exception;
    void fileDelete(String filename);
}
