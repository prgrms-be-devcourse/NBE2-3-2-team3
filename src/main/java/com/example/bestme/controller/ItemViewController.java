package com.example.bestme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/items")
public class ItemViewController {

    @GetMapping
    public String getItems() {
        return "items";
    }

    @GetMapping("/recommend")
    public String getRecommendItems() {
        return "recommend-item";
    }
}
