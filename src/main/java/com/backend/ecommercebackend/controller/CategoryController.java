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
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @GetMapping("/getSpec/{categoryId}")
    public ResponseEntity<List<String>> getAllSpecByCategoryId(@PathVariable int categoryId) {
        return ResponseEntity.ok(service.getSpecsByCategoryId(categoryId));
    }

    @GetMapping("/getAllCategory")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(service.getAllCategories());
    }

    @PostMapping("/createCategory")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        final var createdCategory = service.createCategory(categoryRequest);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{categoryId}").build(createdCategory.getCategoryId());
        return ResponseEntity.created(location).body(createdCategory);
    }
    @PutMapping("/updateCategory/{categoryId}")
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
