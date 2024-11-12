package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.service.ProductService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping("/getAll")
    @Operation(summary = "Bütün məhsulları almaq üçün endpoint")
    public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(service.getAllProduct(token));
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

    @GetMapping("/filterByPriceAndSpecs")
    @Operation(summary = "Məhsulları filter etmək üçün endpoint",
            description = "Bu endpointə min,max,və xüsusi kateqoriyaya görə gələn spesifikasiya adlarını param ilə göndərərək bu filterlərə uyğun məhsulları ala bilərik.")
    public ResponseEntity<List<Product>> getFilteringProducts(@RequestParam(required = false) Float min,
                                                              @RequestParam(required = false) Float max,
                                                              @RequestParam(required = false) Map<String, String> filterSpec){
        return ResponseEntity.ok(service.getFilteringProducts(min,max,filterSpec));
    }

    @PostMapping
    @Operation(summary = "Yeni məhsul əlavə etmək üçün endpoint",description = "Məlumatlar form-data olaraq göndəriləcək.Şəkil əlavə etmək mütləqdir.")
    public ResponseEntity<Product> createProduct(@ModelAttribute ProductRequest request, @RequestParam("imageFile") List<MultipartFile> imageFiles) {
        final var createdProduct = service.addProduct(request, imageFiles);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").build(createdProduct.getId());
        return ResponseEntity.created(location).body(createdProduct);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Məhsulları id ilə güncəlləmək üçün endpoint")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @ModelAttribute ProductRequest request, @RequestParam(value = "imageFile") List<MultipartFile> imageFiles) throws IOException {
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
