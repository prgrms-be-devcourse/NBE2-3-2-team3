package com.example.bestme.controller;

import com.example.bestme.domain.Item;
import com.example.bestme.dto.request.ItemSaveRequest;
import com.example.bestme.dto.response.BrandSelectResponse;
import com.example.bestme.dto.response.CategoryMenuResponse;
import com.example.bestme.dto.response.ColorSelectResponse;
import com.example.bestme.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public String home(Model model) {
        List<Item> items = adminService.getItems();
        model.addAttribute("items", items);
        return "admin/home";
    }

    @PostMapping
    public String saveItem(
            @RequestPart ItemSaveRequest itemSaveRequest,
            @RequestPart MultipartFile image
    ) {
        adminService.saveItem(itemSaveRequest, "");
        return "redirect:/admin";
    }

    @GetMapping("/categories")
    @ResponseBody
    public ResponseEntity<CategoryMenuResponse> getCategories() {
        return ResponseEntity.ok(adminService.getCategoryMenuResponse());
    }

    @GetMapping("/brands")
    @ResponseBody
    public ResponseEntity<List<BrandSelectResponse>> getBrands() {
        System.out.println("AdminController.getBrands");
        return ResponseEntity.ok(adminService.getBrands());
    }

    @GetMapping("/colors")
    @ResponseBody
    public ResponseEntity<List<ColorSelectResponse>> getColors() {
        return ResponseEntity.ok(adminService.getColors());
    }
}
