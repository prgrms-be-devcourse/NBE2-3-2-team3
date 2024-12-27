package com.example.bestme.controller;

import com.example.bestme.dto.request.BrandSaveRequest;
import com.example.bestme.dto.request.CategorySaveRequest;
import com.example.bestme.dto.request.ItemSaveRequest;
import com.example.bestme.dto.response.BrandSelectResponse;
import com.example.bestme.dto.response.CategoryMenuResponse;
import com.example.bestme.dto.response.ColorSelectResponse;
import com.example.bestme.dto.response.ItemDetailResponse;
import com.example.bestme.service.AdminService;
import com.example.bestme.service.LocalImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.example.bestme.util.ImageDirectoryUrl.BRAND_DIRECTORY;
import static com.example.bestme.util.ImageDirectoryUrl.ITEM_DIRECTORY;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final LocalImageService imageService;

    @GetMapping
    public String home(
            Model model,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ItemDetailResponse> pagingItems = adminService.getPagingItems(pageable);
        model.addAttribute("pagingItems", pagingItems);
        return "admin/home";
    }

    @GetMapping("/categories")
    @ResponseBody
    public ResponseEntity<CategoryMenuResponse> getCategories() {
        return ResponseEntity.ok(adminService.getCategoryMenu());
    }

    @GetMapping("/brands")
    @ResponseBody
    public ResponseEntity<List<BrandSelectResponse>> getBrands() {
        return ResponseEntity.ok(adminService.getBrands());
    }

    @GetMapping("/colors")
    @ResponseBody
    public ResponseEntity<List<ColorSelectResponse>> getColors() {
        return ResponseEntity.ok(adminService.getColors());
    }

    @PostMapping("/items")
    public String saveItem(
            @RequestPart ItemSaveRequest itemSaveRequest,
            @RequestPart MultipartFile image,
            RedirectAttributes redirectAttributes
    ) {
        String imageUrl = imageService.save(image, ITEM_DIRECTORY);
        adminService.saveItem(itemSaveRequest, imageUrl);
        redirectAttributes.addAttribute("page", 0);
        return "redirect:/admin";
    }

    @PostMapping("/brands")
    public String saveBrand(
            @RequestPart BrandSaveRequest brandSaveRequest,
            @RequestPart MultipartFile image
    ) {
        String imageUrl = imageService.save(image, BRAND_DIRECTORY);
        adminService.saveBrand(brandSaveRequest, imageUrl);
        return "redirect:/admin";
    }

    @PostMapping("/categories")
    public String saveCategory(
            @RequestBody CategorySaveRequest categorySaveRequest
    ) {
        adminService.saveCategory(categorySaveRequest);
        return "redirect:/admin";
    }
}
