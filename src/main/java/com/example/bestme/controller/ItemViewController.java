package com.example.bestme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/items")
public class ItemViewController {

    @GetMapping
    public String getItems(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String brands,
            @RequestParam(required = false) String colors,
            @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return "items";
    }
}
