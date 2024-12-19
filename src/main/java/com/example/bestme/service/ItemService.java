package com.example.bestme.service;

import com.example.bestme.domain.Brand;
import com.example.bestme.domain.Category;
import com.example.bestme.domain.Color;
import com.example.bestme.domain.Item;
import com.example.bestme.dto.request.SearchConditionRequest;
import com.example.bestme.dto.response.CategoryMenuResponse;
import com.example.bestme.dto.response.FilterMenuResponse;
import com.example.bestme.dto.response.ItemDetailResponse;
import com.example.bestme.dto.response.ItemsResponse;
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
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ColorRepository colorRepository;

    public ItemsResponse getItemsResponseBySearchCondition(SearchConditionRequest searchConditionRequest) {
        return ItemsResponse.from(getItemsBySearchCondition(searchConditionRequest));
    }

    private List<Item> getItemsBySearchCondition(SearchConditionRequest searchConditionRequest) {
        return itemRepository.findItemsBySearchCondition(
                searchConditionRequest.categoryId(),
                searchConditionRequest.brands(),
                searchConditionRequest.colors()
        );
    }

    public ItemDetailResponse getItemDetailResponse(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NoSuchElementException("아이템 없음"));
        return ItemDetailResponse.from(item);
    }

    public CategoryMenuResponse getCategoryMenuResponse() {
        List<Category> categories = categoryRepository.findAllByParentCategoryIdIsNull();
        return CategoryMenuResponse.from(categories);
    }

    public FilterMenuResponse getFilterMenuResponse(Long categoryId) {
        List<Brand> brands = brandRepository.findAllByCategoryId(categoryId);
        List<Color> colors = colorRepository.findAll();
        return FilterMenuResponse.of(brands, colors);
    }
}