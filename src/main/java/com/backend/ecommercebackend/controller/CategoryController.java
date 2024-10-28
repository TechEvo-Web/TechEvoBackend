package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.request.CategoryRequest;
import com.backend.ecommercebackend.dto.response.CategoryResponse;
import com.backend.ecommercebackend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @GetMapping("/getAll")
    @Operation(summary = "Bütün kateqoriya məlumatlarını almaq üçün endpoint")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(service.getAllCategories());
    }

    @PostMapping
    @Operation(summary = "Yeni kateqoriya əlavə etmək üçün endpoint")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        final var createdCategory = service.createCategory(categoryRequest);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{categoryId}").build(createdCategory.getCategoryId());
        return ResponseEntity.created(location).body(createdCategory);
    }

    @GetMapping("/getFilters")
    @Operation(summary = "Filter spesifikasiyalarını almaq üçün endpoint",
            description = "Bura param ilə kateqoriya adı göndəririk.Və geriyə kateqoriyaya özəl seçilmiş filter spesifikasiyalar və bu spesifikasiyalarin productlarda olan fərqli dəyərləri dönür.")
    public ResponseEntity<Object> getFilters(@RequestParam String categoryName) {
        return ResponseEntity.ok(service.getFiltersByCategoryName(categoryName));
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Kateqoriyani id ilə güncəlləmək üçün endpoint")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable int categoryId, @RequestBody CategoryRequest categoryRequest) {
        final var updatedCategory = service.updateCategory(categoryId, categoryRequest);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{categoryId}").build(updatedCategory.getCategoryId());
        return  ResponseEntity.created(location).body(updatedCategory);
    }

    @DeleteMapping("/deleteCategory/{categoryId}")
    @Operation(summary = "Kateqoriyanı categoryİd ilə silmək üçün endpoint")
    public ResponseEntity<Void> deleteCategory(@PathVariable int categoryId) {
         service.deleteCategory(categoryId);
         return ResponseEntity.noContent().build();
    }
}
