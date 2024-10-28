package com.backend.ecommercebackend.controller;
import com.backend.ecommercebackend.dto.request.ProductSpecificationRequest;
import com.backend.ecommercebackend.dto.response.ProductSpecificationResponse;
import com.backend.ecommercebackend.service.SpecificationService;
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
    public ResponseEntity<List<ProductSpecificationResponse>> getAllSpecifications() {
        return ResponseEntity.ok(service.getAllSpecifications());
    }

    @GetMapping("/filterByCategoryName")
    public ResponseEntity<List<String>>getFilterSpecificationsByCategoryName(@RequestParam String categoryName) {
        return ResponseEntity.ok(service.getFilterSpecificationsByCategoryName(categoryName));
    }

    @GetMapping("/getAllBy/{categoryId}")
    public ResponseEntity<List<String>> getAllSpecByCategoryId(@PathVariable int categoryId) {
        return ResponseEntity.ok(service.getSpecsByCategoryId(categoryId));
    }

    @PostMapping
    public ResponseEntity<ProductSpecificationResponse> createSpecification(@RequestBody ProductSpecificationRequest specificationRequest) {
        final var createdSpecification = service.addSpecification(specificationRequest);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/specification/{specificationId}").build(createdSpecification.getSpecificationId());
        return ResponseEntity.created(location).body(createdSpecification);
    }

    @PutMapping("/{specificationId}")
    public ResponseEntity<ProductSpecificationResponse> updateSpecification(@PathVariable Long specificationId, @RequestBody ProductSpecificationRequest specificationRequest) {
        final var updatedSpecification = service.updateSpecification(specificationId, specificationRequest);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/specification/{specificationId}").build(updatedSpecification.getSpecificationId());
        return  ResponseEntity.created(location).body(updatedSpecification);
    }

    @DeleteMapping("/{specificationId}/{categoryId}")
    public ResponseEntity<Void> deleteSpecification(@PathVariable Long specificationId,@PathVariable int categoryId) {
        service.deleteSpecification(specificationId,categoryId);
        return ResponseEntity.noContent().build();
    }
}
