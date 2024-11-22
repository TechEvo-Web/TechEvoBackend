package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.service.ProductService;
import java.io.IOException;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService service;

    @GetMapping("/getAll")
    @Operation(summary = "Bütün məhsulları almaq üçün endpoint")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    @GetMapping("/getAllByCategoryName")
    @Operation(summary = "Məhsulları kateqoriya adı ilə almaq üçün endpoint")
    public ResponseEntity<List<Product>> getProductsByCategory(@RequestParam String categoryName) {
        return ResponseEntity.ok(service.getProductsByCategoryName(categoryName));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Hər hansı məhsulu id ilə almaq üçün endpoint")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    @PostMapping
    @Operation(summary = "Yeni məhsul əlavə etmək üçün endpoint", description = "Məlumatlar form-data olaraq göndəriləcək.Şəkil əlavə etmək mütləqdir.")
    public ResponseEntity<Product> createProduct(@RequestPart(name = "request") ProductRequest request, @RequestParam("imageFile") List<MultipartFile> imageFiles) {
        final var createdProduct = service.addProduct(request, imageFiles);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").build(createdProduct.getId());
        return ResponseEntity.created(location).body(createdProduct);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Məhsulları id ilə güncəlləmək üçün endpoint")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestPart(name = "request",required = false) ProductRequest request, @RequestParam(value = "imageFile",required = false) List<MultipartFile> imageFiles) throws IOException {
        final var updatedProduct = service.updateProduct(id, request, imageFiles);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").build(updatedProduct.getId());
        return ResponseEntity.created(location).body(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Məhsulu id ilə silmək üçün endpoint")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();

    }
}
