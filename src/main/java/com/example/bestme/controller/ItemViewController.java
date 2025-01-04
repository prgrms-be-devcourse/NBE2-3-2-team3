package com.example.bestme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Tag(name = "ItemView", description = "아이템 페이지를 렌더링하기 위한 컨트롤러")
public class ItemViewController {

    @GetMapping
    @Operation( summary = "모든 아이템 페이지", description = "모든 아이템 페이지" )
    public String getItems() {
        return "items";
    }
}
