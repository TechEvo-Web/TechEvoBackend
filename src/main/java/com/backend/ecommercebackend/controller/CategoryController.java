package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.request.CategoryRequest;
import com.backend.ecommercebackend.dto.response.CategoryResponse;
import com.backend.ecommercebackend.service.CategoryService;
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
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(service.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        final var createdCategory = service.createCategory(categoryRequest);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{categoryId}").build(createdCategory.getCategoryId());
        return ResponseEntity.created(location).body(createdCategory);
    }

    @GetMapping("/getFilters")
    public ResponseEntity<Object> getFilters(@RequestParam String categoryName) {
        return ResponseEntity.ok(service.getFiltersByCategoryName(categoryName));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable int categoryId, @RequestBody CategoryRequest categoryRequest) {
        final var updatedCategory = service.updateCategory(categoryId, categoryRequest);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{categoryId}").build(updatedCategory.getCategoryId());
        return  ResponseEntity.created(location).body(updatedCategory);
    }

    @DeleteMapping("/deleteCategory/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int categoryId) {
         service.deleteCategory(categoryId);
         return ResponseEntity.noContent().build();
    }
}
