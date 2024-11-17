package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.RecommendProductRequest;
import com.backend.ecommercebackend.model.product.Product;
import java.util.List;

public interface ProductRecommendService {
  List<Product> findRecommendedProduct(RecommendProductRequest request);
}
