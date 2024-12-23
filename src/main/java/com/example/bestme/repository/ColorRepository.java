package com.example.bestme.repository;

import com.example.bestme.domain.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

public interface ColorRepository extends JpaRepository<Color, Long> {

    default Color getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("퍼스널컬러 찾을 수 없음"));
    }
}
