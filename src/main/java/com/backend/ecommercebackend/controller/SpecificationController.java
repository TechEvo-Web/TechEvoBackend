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
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class SpecificationController {
    private final SpecificationService service;

    @GetMapping("/getAllSpec")
    public ResponseEntity<List<ProductSpecificationResponse>> getAllSpecifications() {
        return ResponseEntity.ok(service.getAllSpecifications());
    }

    @PostMapping("/createSpec")
    public ResponseEntity<ProductSpecificationResponse> createSpecification(@RequestBody ProductSpecificationRequest specificationRequest) {
        final var createdSpecification = service.addSpecification(specificationRequest);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/specification/{specificationId}").build(createdSpecification.getSpecificationId());
        return ResponseEntity.created(location).body(createdSpecification);
    }

    @PutMapping("/specUpdate/{specificationId}")
    public ResponseEntity<ProductSpecificationResponse> updateSpecification(@PathVariable Long specificationId, @RequestBody ProductSpecificationRequest specificationRequest) {
        final var updatedSpecification = service.updateSpecification(specificationId, specificationRequest);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/specification/{specificationId}").build(updatedSpecification.getSpecificationId());
        return  ResponseEntity.created(location).body(updatedSpecification);
    }

    @DeleteMapping("/deleteSpec/{categoryId}/{specificationId}")
    public ResponseEntity<Void> deleteSpecification(@PathVariable Long specificationId,@PathVariable int categoryId) {
        service.deleteSpecification(specificationId,categoryId);
        return ResponseEntity.noContent().build();
    }
}
