package com.example.bestme.service.community;

import com.example.bestme.domain.community.Community;
import com.example.bestme.dto.community.CommunityDTO;
import com.example.bestme.dto.community.WriteDTO;
import com.example.bestme.repository.CommunityRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommunityService {

    private final CommunityRepository communityRepositorySpy;
    private final ModelMapper modelMapper = new ModelMapper();
    private final String uploadPath;

    // 커뮤니티 개발 담당자의 프로젝트 내 -> 이미지 업로드 폴더 (절대 경로) 사용
    /*
    public CommunityService(CommunityRepository communityRepositorySpy, @Value("${file.upload-path}") String uploadPath) {
        this.communityRepositorySpy = communityRepositorySpy;
        this.uploadPath = uploadPath;
    }
     */

    // OS별 루트 폴더 아래 -> 이미지 업로드 폴더 (절대 경로) 사용 시
    /*
    public CommunityService(CommunityRepository communityRepositorySpy
            , @Value("${file.upload-path.unix}") String unixPath
            , @Value("${file.upload-path.windows}") String windowsPath
    ) {
        this.communityRepositorySpy = communityRepositorySpy;

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            this.uploadPath = windowsPath;
        } else {
            this.uploadPath = unixPath;
        }
    }
     */

    public CommunityService(CommunityRepository communityRepositorySpy, @Value("${file.upload-path}") String uploadPath) {
        this.communityRepositorySpy = communityRepositorySpy;
        // 실행 디렉토리를 기준으로 "upload" 폴더 설정
        this.uploadPath = new File(uploadPath).getAbsolutePath();
    }

    @PostConstruct
    public void init() {
        File directory = new File(uploadPath);
        if (!directory.exists() && !directory.mkdirs()) {            // upload 폴더 없을 경우 생성
            throw new RuntimeException("파일 저장 폴더 생성 실패");
        }
    }


    public CommunityDTO createBoard(WriteDTO to, MultipartFile file) {

        if ( to.getUserId() == null ) {throw new IllegalArgumentException("user id가 없습니다.");}
        if ( to.getSubject() == null || to.getSubject().isBlank() ) {throw new IllegalArgumentException("제목 입력은 필수입니다.");}
        if ( to.getContent() == null || to.getContent().isBlank() ) {throw new IllegalArgumentException("내용 입력은 필수입니다.");}

        if ( file != null && !file.isEmpty() ) {
            try {
                String fileName = fileUpload(file);
                to.setImagename(fileName);
            } catch (IOException e) { throw new RuntimeException("[에러]" + e.getMessage());}
        }

        Community entity = modelMapper.map(to, Community.class);
        communityRepositorySpy.save(entity);

        CommunityDTO result = modelMapper.map(entity, CommunityDTO.class);

        return result;
    }

    // 이미지 파일 업로드 메서드
    public String fileUpload(MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();

        String ext = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID() + "_" + ext;

        File saveFile = new File(uploadPath, fileName);
        file.transferTo(saveFile);

        return fileName;
    }

    // 페이지별 게시물 가져오는 메서드
    public List<CommunityDTO> findAllPage() {

        List<Community> boards = communityRepositorySpy.findAll();

        List<CommunityDTO> lists = boards.stream()
                .map(board -> modelMapper.map(board, CommunityDTO.class))
                .collect(Collectors.toList());

        return lists;
    }
}
