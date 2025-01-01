package com.example.bestme.repository;

import com.example.bestme.domain.community.Community;
import com.example.bestme.dto.community.ResponseNavigationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, String> {

    @Modifying
    @Query("update board b set b.view = b.view + 1 where b.boardId = :boardId")
    int updateView(@Param("boardId") Long boardId);

    @Query( "select b, u.nickname from board b join b.user u order by b.boardId" )
    Page<Object[]> findAllBoard(Pageable pageable);

    @Query( "select b, u.nickname from board b join b.user u where u.id = :userId order by b.boardId" )
    Page<Object[]> findAllBoardByUserId(Pageable pageable, Long userId);

    // 지정 게시물 Navigation API ( 어려워서 구현 보류 )
    // 첫 번째 방법
    /*
    @Query(value = "SELECT " +
            "(SELECT c.board_id FROM board c WHERE c.board_id < :boardId ORDER BY c.board_id DESC limit 1 ) AS prevBoardId, " +
            "(SELECT c.subject FROM board c WHERE c.board_id < :boardId ORDER BY c.board_id DESC Limit 1) AS prevBoardSubject, " +
            "(SELECT c.board_id FROM board c WHERE c.board_id > :boardId ORDER BY c.board_id ASC Limit 1) AS nextBoardId, " +
            "(SELECT c.subject FROM board c WHERE c.board_id > :boardId ORDER BY c.board_id ASC Limit 1) AS nextBoardSubject ",
            nativeQuery = true)
    ResponseNavigationDTO boardNavigation(Long boardId);
     */
    // 두 번째 방법
    /*
    @Query(value = "SELECT " +
            "   board_id, " +
            "   subject, " +
            "   LAG(board_id) OVER (ORDER BY board_id) AS prevBoardId, " +
            "   LAG(subject) OVER (ORDER BY board_id) AS prevBoardSubject, " +
            "   LEAD(board_id) OVER (ORDER BY board_id) AS nextBoardId, " +
            "   LEAD(subject) OVER (ORDER BY board_id) AS nextBoardSubject " +
            "FROM board",
            nativeQuery = true)
    List<ResponseNavigationDTO> boardNavigation(Long boardId);
     */
}
