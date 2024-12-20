package com.example.bestme.service.community;

import com.example.bestme.domain.community.Community;
import com.example.bestme.dto.community.ResponseAllBoardDTO;
import com.example.bestme.dto.community.RequestWriteDTO;
import com.example.bestme.dto.community.ResponseFindBoardDTO;
import com.example.bestme.repository.CommunityRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommunityService {

    private final CommunityRepository communityRepositorySpy;
    private final LocalFileService localFileService;
    private final ModelMapper modelMapper = new ModelMapper();

    public CommunityService(CommunityRepository communityRepositorySpy, LocalFileService localFileService) {
        this.communityRepositorySpy = communityRepositorySpy;
        this.localFileService = localFileService;
    }

    // 게시물 insert
    public ResponseFindBoardDTO createBoard(RequestWriteDTO to, MultipartFile file) {

        if ( to.getUserId() == null ) {throw new IllegalArgumentException("user id가 없습니다.");}
        if ( to.getSubject() == null || to.getSubject().isBlank() ) {throw new IllegalArgumentException("제목 입력은 필수입니다.");}
        if ( to.getContent() == null || to.getContent().isBlank() ) {throw new IllegalArgumentException("내용 입력은 필수입니다.");}

        if ( file != null && !file.isEmpty() ) {
            try {
                String fileName = localFileService.fileUpload(file);
                to.setImagename(fileName);
            } catch (IOException e) { throw new RuntimeException("[에러]" + e.getMessage());}
        }

        Community entity = modelMapper.map(to, Community.class);
        communityRepositorySpy.save(entity);

        ResponseFindBoardDTO result = modelMapper.map(entity, ResponseFindBoardDTO.class);
        result.setImageUrl("/upload/" + result.getImagename());

        return result;
    }

    // 페이지별 게시물 가져오는 메서드
    public List<ResponseAllBoardDTO> findAllBoard() {

        List<Community> boards = communityRepositorySpy.findAll();

        if (boards.isEmpty()) {
            throw new IllegalArgumentException("생성된 게시물이 존재하지 않습니다.");
        }

        List<ResponseAllBoardDTO> lists = boards.stream()
                .map(board -> modelMapper.map(board, ResponseAllBoardDTO.class))
                .collect(Collectors.toList());

        return lists;
    }
}

