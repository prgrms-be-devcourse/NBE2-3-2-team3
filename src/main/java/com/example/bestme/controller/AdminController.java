package com.example.bestme.controller;

import com.example.bestme.dto.request.BrandSaveRequest;
import com.example.bestme.dto.request.CategorySaveRequest;
import com.example.bestme.dto.request.ItemSaveRequest;
import com.example.bestme.dto.request.ItemUpdateRequest;
import com.example.bestme.dto.response.*;
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

import java.net.URI;
import java.text.MessageFormat;
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

    @ResponseBody
    @GetMapping("/items/{itemId}")
    public ResponseEntity<ItemReadResponse> getItem(@PathVariable Long itemId) {
        ItemReadResponse response = adminService.getItem(itemId);
        return ResponseEntity.ok(response);
    }

    @ResponseBody
    @GetMapping("/categories")
    public ResponseEntity<CategoryMenuResponse> getCategories() {
        return ResponseEntity.ok(adminService.getCategoryMenu());
    }

    @ResponseBody
    @GetMapping("/brands")
    public ResponseEntity<List<BrandSelectResponse>> getBrands() {
        return ResponseEntity.ok(adminService.getBrands());
    }

    @ResponseBody
    @GetMapping("/colors")
    public ResponseEntity<List<ColorSelectResponse>> getColors() {
        return ResponseEntity.ok(adminService.getColors());
    }

    @PostMapping("/items")
    public ResponseEntity<Void> saveItem(
            @RequestPart ItemSaveRequest itemSaveRequest,
            @RequestPart MultipartFile image
    ) {
        String imageUrl = imageService.save(image, ITEM_DIRECTORY);
        Long itemId = adminService.saveItem(itemSaveRequest, imageUrl);
        return ResponseEntity.created(URI.create(MessageFormat.format("/api/items/{0}", itemId))).build();
    }

    @PostMapping("/brands")
    public ResponseEntity<Void> saveBrand(
            @RequestPart BrandSaveRequest brandSaveRequest,
            @RequestPart MultipartFile image
    ) {
        String imageUrl = imageService.save(image, BRAND_DIRECTORY);
        Long brandId = adminService.saveBrand(brandSaveRequest, imageUrl);
        return ResponseEntity.created(URI.create(MessageFormat.format("/api/brands/{0}", brandId))).build();
    }

    @PostMapping("/categories")
    public ResponseEntity<Void> saveCategory(
            @RequestBody CategorySaveRequest categorySaveRequest
    ) {
        Long categoryId = adminService.saveCategory(categorySaveRequest);
        return ResponseEntity.created(URI.create(MessageFormat.format("/api/categories/{0}", categoryId))).build();
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<Void> updateItem(
            @PathVariable Long itemId,
            @RequestPart ItemUpdateRequest itemUpdateRequest,
            @RequestPart(required = false) MultipartFile image
    ) {
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = imageService.save(image, ITEM_DIRECTORY);
        }
        adminService.updateItem(itemId, itemUpdateRequest, imageUrl);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        adminService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
