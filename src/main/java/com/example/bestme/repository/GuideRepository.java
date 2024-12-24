package com.example.bestme.repository;

import com.example.bestme.domain.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GuideRepository extends JpaRepository<Guide, Long> {
    List<Guide> findAllByColorId(Long colorId);
}
