package com.example.bestme.service;

import com.example.bestme.domain.Category;
import com.example.bestme.domain.Color;
import com.example.bestme.domain.Item;
import com.example.bestme.dto.request.FilterRequest;
import com.example.bestme.dto.response.FilterDataResponse;
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
//    private final ColorRepository colorRepository;
//    private final CategoryRepository categoryRepository;

    public ItemsResponse getItemsResponseByFilter(FilterRequest filterRequest) {
        return ItemsResponse.of(
                getItemCount(filterRequest),
                getItemsByFilter(filterRequest)
        );
    }

    private long getItemCount(FilterRequest filterRequest) {
        return itemRepository.findItemCount(
                filterRequest.categories(),
                filterRequest.colors()
        );
    }

    private List<Item> getItemsByFilter(FilterRequest filterRequest) {
        return itemRepository.findItemsByFilter(
                filterRequest.categories(),
                filterRequest.colors()
        );
    }

//    public ItemDetailResponse getItemDetailResponse(Long id) {
//        Item item = itemRepository.findById(id).orElseThrow(() -> new NoSuchElementException("아이템 x"));
//        return ItemDetailResponse.from(item);
//    }

//    public FilterDataResponse getFilterDataResponse() {
//        List<Category> categories = categoryRepository.findAll();
//        List<Color> colors = colorRepository.findAll();
//        return FilterDataResponse.of(categories, colors);
//    }
}
