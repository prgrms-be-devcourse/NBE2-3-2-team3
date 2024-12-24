package com.example.bestme.service.community;

import com.example.bestme.domain.community.Community;
import com.example.bestme.domain.user.User;
import com.example.bestme.dto.community.*;
import com.example.bestme.repository.CommunityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional      // 트랜잭션 관리 어노테이션 ( 최적화 필요 시 - @Transactional(readOnly = true)로 특정 작업 전용 설정 )
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final LocalFileService localFileService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final PasswordEncoder passwordEncoder;

    public CommunityService(CommunityRepository communityRepository, LocalFileService localFileService, PasswordEncoder passwordEncoder) {
        this.communityRepository = communityRepository;
        this.localFileService = localFileService;
        this.passwordEncoder = passwordEncoder;
    }

    // DTO 유효성 검사 메서드
    public void validateBoardDTO(BoardDTO dto)  { dto.validate(); }

    // 게시물 생성 기능
    public ResponseFindDTO createBoard(RequestWriteDTO to, MultipartFile file) {
        // DTO 유효성 검사
        validateBoardDTO(to);

        if ( file != null && !file.isEmpty() ) {
            try {
                String fileName = localFileService.fileUpload(file);
                to.setImagename(fileName);
            } catch (IOException e) { throw new IllegalArgumentException("파일 처리 중 오류가 발생했습니다." + e.getMessage());} }

        Community entity = modelMapper.map(to, Community.class);
        communityRepository.save(entity);

        ResponseFindDTO result = modelMapper.map(entity, ResponseFindDTO.class);

        return result;
    }

    // 게시물 목록 읽기 기능
    public Page<ResponseAllDTO> findAllBoard(Integer page, Integer numberOfDataPerPage) {

        Pageable pageable = PageRequest.of(page, numberOfDataPerPage, Sort.by(Sort.Direction.DESC, "boardId"));
        Page<Community> boards = communityRepository.findAll(pageable);

        if (boards.getNumberOfElements() == 0) { throw new IllegalArgumentException("존재하지 않는 페이지입니다."); }

        Page<ResponseAllDTO> lists = boards.map(board -> modelMapper.map(board, ResponseAllDTO.class));
        return lists;
    }

    // 지정 게시물 읽기 기능
    @Transactional
    public ResponseFindDTO findBoardById(String boardId) {
        // 조회수 증가 처리
        int updateView = communityRepository.updateView(Long.valueOf(boardId));
        if(updateView == 0) { throw new IllegalArgumentException("해당 게시물은 존재하지 않습니다." + boardId); }

        Community board = communityRepository.findById(boardId)         // communityRepository의 인자 타입이 String일 때 사용
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다." + boardId));

        ResponseFindDTO result = modelMapper.map(board, ResponseFindDTO.class);
        return result;
    }

    // 지정 게시물 네이게이션(이전글, 다음글) 반환 기능 ( 구현 보류 - 너무 어려움 )
    /*
    public List<ResponseNavigationDTO> boardNavigation(Long boardId) {
        List<ResponseNavigationDTO> results = communityRepository.boardNavigation(boardId);

        System.out.println(results);

        return results;
    }
     */

    // 지정 게시물 수정 기능
    public ResponseFindDTO modifyBoard(RequestModifyDTO to, MultipartFile file) {
        // DTO 유효성 검사
        validateBoardDTO(to);

        // 기존 게시물 읽어오기
        Community board = communityRepository.findById(String.valueOf(to.getBoardId()))
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다." + to.getBoardId()));

        // 기존 게시물 데이터에 수정된 게시물 덮어쓰기
        board.setSubject(to.getSubject());
        board.setContent(to.getContent());
        board.setCategory(to.getCategory());

        // 이미지 파일 변경(다른 파일명) 및 삭제(null) 경우
        try {
            if ( file != null && !file.isEmpty() ) {
                String fileName = localFileService.fileUpload(file);
                // 기존 파일 삭제 (선택사항)
                if (board.getImagename() != null) { localFileService.fileDelete(board.getImagename()); }

                board.setImagename(fileName);

            } else if (to.getImagename() == null) {         // 파일 삭제 요청
                // 기존 파일 삭제 (선택사항)
                if (board.getImagename() != null) { localFileService.fileDelete(board.getImagename()); }

                board.setImagename(null);
            }
        } catch (IOException e) { throw new IllegalArgumentException("파일 처리 중 오류가 발생했습니다." + e.getMessage()); }

        communityRepository.save(board);
        ResponseFindDTO result = modelMapper.map(board, ResponseFindDTO.class);

        return result;
    }

    public ResponseDeleteDTO deleteBoard(RequestDeleteDTO to) {
        // DTO 유효성 검사
        validateBoardDTO(to);

        Community board = communityRepository.findById(String.valueOf(to.getBoardId()))
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다." + to.getBoardId()));

        // 유효성 검사 ( userId, nickname )
        if( !to.getUserId().equals(board.getUserId()) ) {
            throw new IllegalArgumentException("사용자 id가 일치하지 않습니다.");
        }
        // user 엔티티와 연결 후 사용
        /*
        // 게시물 작성자와 요청자 정보 비교
        User user = board.getUser();

        // 사용자 ID와 닉네임이 일치하는지 확인
        if (!to.getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("사용자 ID가 일치하지 않습니다.");
        }
        if (!to.getNickname().equals(user.getNickname())) {
            throw new IllegalArgumentException("닉네임이 일치하지 않습니다.");
        }

        // 비밀번호 확인 (현재 인증된 사용자의 비밀번호와 비교)
        // passwordEncoder.matches : 사용자가 입력한 평문 비밀번호와 저장된 암호화된 비밀번호가 일치하는지를 확인
        if (!passwordEncoder.matches(to.getPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
         */

        // entity 삭제 전, success 반환 값 미리 저장
        ResponseDeleteDTO result = modelMapper.map(board, ResponseDeleteDTO.class);
        // 파일 삭제
        if (board.getImagename() != null) { localFileService.fileDelete(board.getImagename()); }
        // 게시물 삭제
        communityRepository.delete(board);

        return result;
    }
}

