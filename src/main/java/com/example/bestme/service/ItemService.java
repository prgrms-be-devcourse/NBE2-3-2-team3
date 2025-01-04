package com.example.bestme.service;

import com.example.bestme.domain.Brand;
import com.example.bestme.domain.Category;
import com.example.bestme.domain.Color;
import com.example.bestme.domain.Item;
import com.example.bestme.dto.api.ResultResponse;
import com.example.bestme.dto.request.SearchConditionRequest;
import com.example.bestme.dto.response.*;
import com.example.bestme.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ColorRepository colorRepository;
    private final ResultRepository resultRepository;

    public ItemsResponse getSliceItemsResponseBySearchCondition(SearchConditionRequest searchConditionRequest, Pageable pageable) {
        return ItemsResponse.from(getSliceItemsBySearchCondition(searchConditionRequest, pageable));
    }

    private Slice<Item> getSliceItemsBySearchCondition(SearchConditionRequest searchConditionRequest, Pageable pageable) {
        return itemRepository.findSliceItemsBySearchCondition(
                searchConditionRequest.categoryId(),
                searchConditionRequest.brands(),
                searchConditionRequest.colors(),
                pageable
        );
    }

    public ItemDetailResponse getItemDetailResponse(Long itemId) {
        Item item = itemRepository.getById(itemId);
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

    public RecommendItemsResponse getRecommendItemsResponse(Long userId) {
        ResultResponse.ReadColorResponseDTO resultColor = resultRepository.findColorId(userId);
        List<Category> categories = categoryRepository.findAllByParentCategoryIdIsNull();
        Map<String, List<Item>> items = new HashMap<>();

        for (Category category : categories) {
            items.put(
                    category.getName(),
                    itemRepository.findRecommendItems(category.getId(), resultColor.getColorId())
            );
        }

        return RecommendItemsResponse.of(categories, items);
    }
}