package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.RecommendProductRequest;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.repository.product.ProductRepository;
import com.backend.ecommercebackend.service.ProductRecommendService;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class ProductRecommendServiceImpl implements ProductRecommendService {

  private final ProductRepository productRepository;
  private final Random random = new Random();

  @Override
  public List<Product> findRecommendedProduct(RecommendProductRequest request) {
    String usingPurpose = request.getUsingPurpose();
    String whereUse = request.getWhereUse();
    String look = request.getLook();
    return productRepository.findAll().stream()
            .filter(product -> usingPurpose != null && usingPurpose.trim().equalsIgnoreCase(product.getUsingPurpose().trim()))
            .filter(product -> whereUse != null && whereUse.trim().equalsIgnoreCase(product.getWhereUse().trim()))
            .filter(product -> look != null && look.trim().equalsIgnoreCase(product.getLook().trim()))
            .toList();
  }
}
