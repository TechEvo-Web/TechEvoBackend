package com.backend.ecommercebackend.controller;


import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.service.RecommendationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Slf4j
public class ProductRecommendController {

  private final RecommendationService service;

  @GetMapping("/recommendations")
  public ResponseEntity<List<Product>> getRecommendedProducts(@RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    var response= service.getRandomRecommendations(token);
    return ResponseEntity.ok(response);
  }
}
