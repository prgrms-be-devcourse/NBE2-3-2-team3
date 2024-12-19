package com.example.bestme.service;

import com.example.bestme.domain.Category;
import com.example.bestme.domain.Color;
import com.example.bestme.dto.response.CategoryMenuResponse;
import com.example.bestme.dto.response.ColorMenuResponse;
import com.example.bestme.repository.CategoryRepository;
import com.example.bestme.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;

    public CategoryMenuResponse getCategoryMenuResponse() {
        List<Category> categories = categoryRepository.findAllByParentCategoryIdIsNull();
        return CategoryMenuResponse.from(categories);
    }

    public ColorMenuResponse getColorMenuResponse() {
        List<Color> colors = colorRepository.findAll();
        return ColorMenuResponse.from(colors);
    }
}
