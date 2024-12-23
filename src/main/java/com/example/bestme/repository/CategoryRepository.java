package com.example.bestme.repository;

import com.example.bestme.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.NoSuchElementException;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    default Category getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("카테고리 찾을 수 없음"));
    }

    List<Category> findAllByParentCategoryIdIsNull();
}