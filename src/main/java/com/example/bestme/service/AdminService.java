package com.example.bestme.service;

import com.example.bestme.domain.Brand;
import com.example.bestme.domain.Category;
import com.example.bestme.domain.Color;
import com.example.bestme.domain.Item;
import com.example.bestme.dto.request.BrandSaveRequest;
import com.example.bestme.dto.request.CategorySaveRequest;
import com.example.bestme.dto.request.ItemSaveRequest;
import com.example.bestme.dto.response.BrandSelectResponse;
import com.example.bestme.dto.response.CategoryMenuResponse;
import com.example.bestme.dto.response.ColorSelectResponse;
import com.example.bestme.dto.response.ItemDetailResponse;
import com.example.bestme.repository.BrandRepository;
import com.example.bestme.repository.CategoryRepository;
import com.example.bestme.repository.ColorRepository;
import com.example.bestme.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ColorRepository colorRepository;

    public Page<ItemDetailResponse> getPagingItems(Pageable pageable) {
        return itemRepository.findAll(pageable)
                .map(ItemDetailResponse::from);
    }

    public CategoryMenuResponse getCategoryMenu() {
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
        Color color = colorRepository.getById(itemSaveRequest.colorId());
        Category category = categoryRepository.getById(itemSaveRequest.categoryId());
        Brand brand = brandRepository.getById(itemSaveRequest.brandId());
        Item item = itemSaveRequest.toEntity(color, category, brand, imageUrl);

        return itemRepository.save(item).getId();
    }

    @Transactional
    public Long saveBrand(BrandSaveRequest brandSaveRequest, String imageUrl) {
        Brand brand = brandSaveRequest.toEntity(imageUrl);
        return brandRepository.save(brand).getId();
    }

    @Transactional
    public Long saveCategory(CategorySaveRequest categorySaveRequest) {
        Category parentCategory = null;
        if (!categorySaveRequest.parentCategoryId().equals("선택하세요")) {
            Long parentCategoryId = Long.valueOf(categorySaveRequest.parentCategoryId());
            parentCategory = categoryRepository.getById(parentCategoryId);
        }
        Category category = categorySaveRequest.toEntity(parentCategory);

        return categoryRepository.save(category).getId();
    }
}
