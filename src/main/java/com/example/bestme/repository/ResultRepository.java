package com.example.bestme.repository;

import com.example.bestme.domain.Result;
import com.example.bestme.dto.api.ResultResponse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    // 사용자의 모든 결과 조회
    List<Result> findAllByUser_id(Long userId);

    // 사용자 별로 가장 많은 빈도의 colorid를 찾는 쿼리
    @Query(value = "SELECT color_id, COUNT(color_id) AS frequency FROM result WHERE user_id = :userId GROUP BY color_id ORDER BY frequency DESC LIMIT 1", nativeQuery = true)
    ResultResponse.ReadColorResponseDTO findColorId(Long userId);
}
