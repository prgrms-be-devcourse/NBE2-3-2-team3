package com.example.bestme.service;

import com.example.bestme.domain.Category;
import com.example.bestme.domain.Color;
import com.example.bestme.domain.Item;
import com.example.bestme.dto.request.FilterRequest;
import com.example.bestme.dto.response.FilterMenuResponse;
import com.example.bestme.dto.response.ItemDetailResponse;
import com.example.bestme.dto.response.ItemsResponse;
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
    private final ColorRepository colorRepository;

    public ItemsResponse getItemsResponseByFilterV1(FilterRequest filterRequest) {
        return ItemsResponse.from(getItemsByFilterV1(filterRequest));
    }

    public ItemsResponse getItemsResponseByFilterV2(Long categoryId, List<String> colors) {
        return ItemsResponse.from(getItemsByFilterV2(categoryId, colors));
    }

    private List<Item> getItemsByFilterV1(FilterRequest filterRequest) {
        return itemRepository.findItemsByFilterV1(
                filterRequest.categories(),
                filterRequest.colors()
        );
    }

    private List<Item> getItemsByFilterV2(Long categoryId, List<String> colors) {
        return itemRepository.findItemsByFilterV2(categoryId, colors);
    }

    public ItemDetailResponse getItemDetailResponse(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NoSuchElementException("아이템 x"));
        return ItemDetailResponse.from(item);
    }

    public FilterMenuResponse getFilterMenuResponse() {
        List<Category> categories = categoryRepository.findAllByParentCategoryIdIsNull();
        List<Color> colors = colorRepository.findAll();
        return FilterMenuResponse.of(categories, colors);
    }
}
