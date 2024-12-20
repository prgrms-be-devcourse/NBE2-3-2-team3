package com.example.bestme.service;

import com.example.bestme.domain.Brand;
import com.example.bestme.domain.Category;
import com.example.bestme.domain.Color;
import com.example.bestme.domain.Item;
import com.example.bestme.dto.request.ItemSaveRequest;
import com.example.bestme.dto.response.BrandSelectResponse;
import com.example.bestme.dto.response.CategoryMenuResponse;
import com.example.bestme.dto.response.ColorSelectResponse;
import com.example.bestme.repository.BrandRepository;
import com.example.bestme.repository.CategoryRepository;
import com.example.bestme.repository.ColorRepository;
import com.example.bestme.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ColorRepository colorRepository;

    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    public CategoryMenuResponse getCategoryMenuResponse() {
        List<Category> categories = categoryRepository.findAllByParentCategoryIdIsNull();
        return CategoryMenuResponse.from(categories);
    }

    public List<BrandSelectResponse> getBrands() {
        return brandRepository.findAll().stream()
                .map(BrandSelectResponse::from)
                .toList();
    }

    public List<ColorSelectResponse> getColors() {
        return colorRepository.findAll().stream()
                .map(ColorSelectResponse::from)
                .toList();
    }

    @Transactional
    public Long saveItem(ItemSaveRequest itemSaveRequest, String imageUrl) {
        Color color = colorRepository.findById(itemSaveRequest.colorId()).orElseThrow(() -> new NoSuchElementException("브랜드 없음"));
        Category category = categoryRepository.findById(itemSaveRequest.categoryId()).orElseThrow(() -> new NoSuchElementException("카테고리 없음"));
        Brand brand = brandRepository.findById(itemSaveRequest.brandId()).orElseThrow(() -> new NoSuchElementException("브랜드 없음"));
        Item item = itemSaveRequest.toEntity(color, category, brand, imageUrl);

        return itemRepository.save(item).getId();
    }
}
