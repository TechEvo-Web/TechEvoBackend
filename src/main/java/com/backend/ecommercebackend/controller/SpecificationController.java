package com.backend.ecommercebackend.controller;
import com.backend.ecommercebackend.dto.request.ProductSpecificationRequest;
import com.backend.ecommercebackend.model.product.ProductSpecification;
import com.backend.ecommercebackend.service.SpecificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product/specification")
@RequiredArgsConstructor
public class SpecificationController {
    private final SpecificationService service;

    @GetMapping("/getAll")
    @Operation(summary = "Bütün spesifikasiyaları almaq üçün endpoint")
    public ResponseEntity<List<ProductSpecification>> getAllSpecifications() {
        return ResponseEntity.ok(service.getAllSpecifications());
    }

    @GetMapping("/filterByCategoryName")
    public ResponseEntity<List<String>>getFilterSpecificationsByCategoryName(@RequestParam String categoryName) {
        return ResponseEntity.ok(service.getFilterSpecificationsByCategoryName(categoryName));
    }

    @GetMapping("/getAllSpec/{categoryId}")
    @Operation(summary = "Kateqoriya id ilə bütün spesifikasiyaları almaq üçün endpoint")

    public ResponseEntity<List<String>> getAllSpecByCategoryId(@PathVariable int categoryId) {
        return ResponseEntity.ok(service.getSpecsByCategoryId(categoryId));
    }

    @PostMapping
    @Operation(summary = "Yeni spefikasiyalari kateqoriyaya uyğun əlavə etmək üçün endpoint")
    public ResponseEntity<ProductSpecification> createSpecification(@RequestBody ProductSpecificationRequest specificationRequest) {
        final var createdSpecification = service.addSpecification(specificationRequest);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/specification/{specificationId}").build(createdSpecification.getSpecificationId());
        return ResponseEntity.created(location).body(createdSpecification);
    }

    @PutMapping("/{specificationId}")
    @Operation(summary = "Spesifikasiyaları güncəlləmək üçün endpoint")
    public ResponseEntity<ProductSpecification> updateSpecification(@PathVariable Long specificationId, @RequestBody ProductSpecificationRequest specificationRequest) {
        final var updatedSpecification = service.updateSpecification(specificationId, specificationRequest);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/specification/{specificationId}").build(updatedSpecification.getSpecificationId());
        return  ResponseEntity.created(location).body(updatedSpecification);
    }

    @DeleteMapping("/{specificationId}/{categoryId}")
    @Operation(summary = "Spesifikasiyaları silmək üçün endpoint")
    public ResponseEntity<Void> deleteSpecification(@PathVariable Long specificationId,@PathVariable int categoryId) {
        service.deleteSpecification(specificationId,categoryId);
        return ResponseEntity.noContent().build();
    }
}
