package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.model.product.Product;
import java.util.List;

public interface RecommendationService {

  List<Product> getRandomRecommendations(String token) ;
}
