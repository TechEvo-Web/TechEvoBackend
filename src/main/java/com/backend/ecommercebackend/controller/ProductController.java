package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import com.backend.ecommercebackend.service.ProductService;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/product/")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService service;

  @GetMapping("/getAllProducts")
  public ResponseEntity<List<ProductResponse>> getAllProducts(){
    return ResponseEntity.ok(service.getAllProduct());
  }
  @GetMapping
  public ResponseEntity<List<ProductResponse>>getProductsByCategory(@RequestParam String categoryName){
    return ResponseEntity.ok(service.getProductsByCategoryName(categoryName));
  }
  @PostMapping("/{id}/rate")
  public ResponseEntity<ProductResponse> rateProduct(@PathVariable Long id, @RequestParam float rating) {
    return ResponseEntity.ok(service.rateProduct(id, rating));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
    return ResponseEntity.ok(service.getProductById(id));
  }

  @PostMapping
  public ResponseEntity<ProductResponse> createProduct(@ModelAttribute ProductRequest request, @RequestParam("imageFile") List<MultipartFile> imageFiles) {
    final var createdProduct = service.addProduct(request, imageFiles);
    final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").build(createdProduct.getId());
    return ResponseEntity.created(location).body(createdProduct);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,@ModelAttribute ProductRequest request, @RequestParam(value="imageFile")List<MultipartFile> imageFiles) throws IOException {
    final var updatedProduct = service.updateProduct(id, request, imageFiles);
    final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").build(updatedProduct.getId());
    return ResponseEntity.created(location).body(updatedProduct);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    service.deleteProduct(id);
    return ResponseEntity.noContent().build();

  }
}
