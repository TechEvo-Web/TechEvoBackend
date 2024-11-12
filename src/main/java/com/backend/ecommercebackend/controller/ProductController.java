package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import com.backend.ecommercebackend.dto.request.RecommendProductRequest;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.service.ProductService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
private final Random random = new Random();
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
    @PostMapping("/recommend")
    @Operation(summary = "Komputer meslehet gormek ucun endpoint")
    public ResponseEntity<?> recommendComputer(@RequestBody RecommendProductRequest request) {
        final var recommendedProductList= service.findRecommendedProduct(request);

        if (!recommendedProductList.isEmpty()) {
            int randomIndex = random.nextInt(recommendedProductList.size());
           Product recommendedProduct= recommendedProductList.get(randomIndex);
            return ResponseEntity.status(HttpStatus.CREATED).body(recommendedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Uygun mehsul tapilmadi");
        }

    }
    }

