package com.example.bestme.service;

import com.example.bestme.domain.Brand;
import com.example.bestme.domain.Category;
import com.example.bestme.domain.Color;
import com.example.bestme.domain.Item;
import com.example.bestme.dto.request.FilterRequest;
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

    public ItemsResponse getItemsResponseByFilter(FilterRequest filterRequest) {
        return ItemsResponse.from(getItemsByFilterV2(filterRequest));
    }

    private List<Item> getItemsByFilterV2(FilterRequest filterRequest) {
        return itemRepository.findItemsByFilter(
                filterRequest.categoryId(),
                filterRequest.brands(),
                filterRequest.colors()
        );
    }

    public ItemDetailResponse getItemDetailResponse(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NoSuchElementException("아이템이 없음"));
        return ItemDetailResponse.from(item);
    }

    public FilterMenuResponse getFilterMenuResponse() {
        List<Category> categories = categoryRepository.findAllByParentCategoryIdIsNull();
        List<Brand> brands = brandRepository.findAll();
        List<Color> colors = colorRepository.findAll();
        return FilterMenuResponse.of(categories, brands, colors);
    }
}
