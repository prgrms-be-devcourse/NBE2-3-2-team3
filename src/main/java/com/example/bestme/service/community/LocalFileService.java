package com.example.bestme.service.community;

import com.example.bestme.dto.community.ResponseFileDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class LocalFileService implements FileService {

    private final String uploadPath;

    public LocalFileService(@Value("${file.upload-path}") String uploadPath) {
        this.uploadPath = new File(uploadPath).getAbsolutePath();
    }

    @PostConstruct
    public void init() {
        File directory = new File(uploadPath);
        if (!directory.exists() && !directory.mkdirs()) {            // upload 폴더 없을 경우 생성
            throw new RuntimeException("파일 저장 폴더 생성 실패");
        }
    }

    @Override
    public String fileUpload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        String mimeType = file.getContentType();

        // 허용된 확장자 목록
        List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp", ".svg");
        // 허용된 Mime 타입 목록
        List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/webp", "image/svg+xml");

        // 업로드된 파일의 확장자가 허용된 확장자가 아니면 예외 발생
        if( !allowedExtensions.contains(ext) ) {
            throw new IllegalArgumentException("허용되지 않은 파일 형식입니다. 허용된 확장자: " + allowedExtensions);
        }
        if( !allowedMimeTypes.contains(mimeType) ) {
            throw new IllegalArgumentException("허용되지 않은 파일 형식입니다.");
        }

        fileName = UUID.randomUUID() + "_" + ext;

        File saveFile = new File(uploadPath, fileName);
        file.transferTo(saveFile);

        return fileName;
    }

    @Override
    public ResponseFileDTO fileFind(String filename) {
        File file = new File(uploadPath, filename);
        Path path = file.toPath();      // 파일의 path를 Path 객체로 생성

        // 파일 존재 및 읽기 가능 여부 확인
        if (!file.exists() || !Files.isReadable(path)) {
            throw new IllegalArgumentException("파일을 찾을 수 없거나 읽을 수 없습니다. 파일명: " + filename);
        }

        // Content-Type 설정 (예: image/jpeg, image/png 등)
        String contentType;
        try {
            // Files.probeContentType(path) : 파일의 MIME 타입을 추정하는 메서드
            contentType = Files.probeContentType(path);
            if (contentType == null) { contentType = "application/octet-stream"; }
        } catch (IOException e) {
            throw new IllegalArgumentException("Content-Type을 지정할 수 없습니다.");
        }

        // ResponseFileDTO 생성
        ResponseFileDTO result = new ResponseFileDTO();
        result.setResource(new FileSystemResource(file));
        result.setContentType(contentType);

        return result;
    }

    @Override
    public void fileDelete(String filename) {
        File file = new File(uploadPath, filename);

        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                throw new IllegalArgumentException("파일 삭제에 실패했습니다. " + filename);
            }
        } else {
            throw new IllegalArgumentException("파일이 존재하지 않습니다. " + filename);
        }
    }
}
