package com.example.bestme.controller;

import com.example.bestme.dto.request.BrandSaveRequest;
import com.example.bestme.dto.request.CategorySaveRequest;
import com.example.bestme.dto.request.ItemSaveRequest;
import com.example.bestme.dto.request.ItemUpdateRequest;
import com.example.bestme.dto.response.*;
import com.example.bestme.service.AdminService;
import com.example.bestme.service.LocalImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Admin", description = "상품, 브랜드, 카테고리를 관리하는 관리자 페이지")
public class AdminController {

    private final AdminService adminService;
    private final LocalImageService imageService;

    @GetMapping
    @Operation( summary = "관리자 홈페이지", description = "모든 아이템 등록 수정 삭제, 브랜드 등록, 카테고리를 추가할 수 있는 페이지" )
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
    @Operation( summary = "등록된 아이템의 상세 정보", description = "아이템 수정 시 기존에 등록된 아이템 정보를 보여주기 위한 API" )
    public ResponseEntity<ItemReadResponse> getItem(@PathVariable Long itemId) {
        ItemReadResponse response = adminService.getItem(itemId);
        return ResponseEntity.ok(response);
    }

    @ResponseBody
    @GetMapping("/categories")
    @Operation( summary = "등록된 카테고리 정보", description = "아이템 등록 시 카테고리 셀렉트 박스에 모든 카테고리를 보여주기 위한 API" )
    public ResponseEntity<CategoryMenuResponse> getCategories() {
        return ResponseEntity.ok(adminService.getCategoryMenu());
    }

    @ResponseBody
    @GetMapping("/brands")
    @Operation( summary = "등록된 브랜드 정보", description = "아이템 등록 시 브랜드 선택 셀렉트 박스에 모든 브랜드를 보여주기 위한 API" )
    public ResponseEntity<List<BrandSelectResponse>> getBrands() {
        return ResponseEntity.ok(adminService.getBrands());
    }

    @ResponseBody
    @GetMapping("/colors")
    @Operation( summary = "등록된 퍼스널컬러 정보", description = "아이템 등록 시 퍼스널컬러 선택 셀렉트 박스에 모든 퍼스널컬러를 보여주기 위한 API" )
    public ResponseEntity<List<ColorSelectResponse>> getColors() {
        return ResponseEntity.ok(adminService.getColors());
    }

    @PostMapping("/items")
    @Operation( summary = "아이템 등록", description = "아이템 등록 API, 등록 시 모든 입력 항목은 필수" )
    public ResponseEntity<Void> saveItem(
            @RequestPart ItemSaveRequest itemSaveRequest,
            @RequestPart MultipartFile image
    ) {
        String imageUrl = imageService.save(image, ITEM_DIRECTORY);
        Long itemId = adminService.saveItem(itemSaveRequest, imageUrl);
        return ResponseEntity.created(URI.create(MessageFormat.format("/api/items/{0}", itemId))).build();
    }

    @PostMapping("/brands")
    @Operation( summary = "브랜드 등록", description = "브랜드 등록 API, 등록 시 모든 입력 항목은 필수" )
    public ResponseEntity<Void> saveBrand(
            @RequestPart BrandSaveRequest brandSaveRequest,
            @RequestPart MultipartFile image
    ) {
        String imageUrl = imageService.save(image, BRAND_DIRECTORY);
        Long brandId = adminService.saveBrand(brandSaveRequest, imageUrl);
        return ResponseEntity.created(URI.create(MessageFormat.format("/api/brands/{0}", brandId))).build();
    }

    @PostMapping("/categories")
    @Operation( summary = "카테고리 추가", description = "카테고리 추가 API, 카테고리를 선택하고 추가하자고 하는 카테고리를 입력하면 해당 카테고리 하위에 생성" )
    public ResponseEntity<Void> saveCategory(
            @RequestBody CategorySaveRequest categorySaveRequest
    ) {
        Long categoryId = adminService.saveCategory(categorySaveRequest);
        return ResponseEntity.created(URI.create(MessageFormat.format("/api/categories/{0}", categoryId))).build();
    }

    @PatchMapping("/items/{itemId}")
    @Operation( summary = "아이템 수정", description = "아이템 수정 API, 입력된 값들만 수정" )
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
    @Operation( summary = "아이템 삭제", description = "아이템 삭제 API" )
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        adminService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
