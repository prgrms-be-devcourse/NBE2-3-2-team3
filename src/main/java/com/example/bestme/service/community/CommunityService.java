package com.example.bestme.service.community;

import com.example.bestme.domain.community.Community;
import com.example.bestme.dto.community.ResponseAllBoardDTO;
import com.example.bestme.dto.community.RequestWriteDTO;
import com.example.bestme.dto.community.ResponseFindBoardDTO;
import com.example.bestme.repository.CommunityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CommunityService {

    private final CommunityRepository communityRepositorySpy;
    private final LocalFileService localFileService;
    private final ModelMapper modelMapper = new ModelMapper();
//    private final Integer numberOfDataPerPage = 10;     // 페이지당 데이터 갯수
//    private final PagedResourcesAssembler<Community> assembler;

    public CommunityService(CommunityRepository communityRepositorySpy, LocalFileService localFileService) {
        this.communityRepositorySpy = communityRepositorySpy;
        this.localFileService = localFileService;
    }

    // 게시물 생성 기능
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

    // 게시물 목록 반환 기능
    public Page<ResponseAllBoardDTO> findAllBoard(Integer page, Integer numberOfDataPerPage) {

        Pageable pageable = PageRequest.of(page, numberOfDataPerPage, Sort.by(Sort.Direction.DESC, "boardId"));

        Page<Community> boards = communityRepositorySpy.findAll(pageable);

        if (boards.getNumberOfElements() == 0) {
            throw new IllegalArgumentException("존재하지 않는 페이지입니다.");
        }

        // 페이지 기준 이동 링크만 첨부 ( 게시물 상세내용 작업 시, 각 페이지 이동 링크 첨부 )
//        PagedModel<EntityModel<ResponseAllBoardDTO>> lists =  pagedResourcesAssembler
//                .toModel(boards, board -> EntityModel
//                        .of(modelMapper.map(board, ResponseAllBoardDTO.class)));

        Page<ResponseAllBoardDTO> lists = boards.map(board -> modelMapper.map(board, ResponseAllBoardDTO.class));

        return lists;
    }
}

