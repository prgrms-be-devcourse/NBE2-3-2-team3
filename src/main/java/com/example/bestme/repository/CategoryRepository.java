package com.example.bestme.repository;

import com.example.bestme.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByParentCategoryIdIsNull();
}