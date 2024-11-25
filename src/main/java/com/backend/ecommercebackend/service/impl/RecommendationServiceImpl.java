package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.authentication.jwt.JwtService;
import com.backend.ecommercebackend.dto.response.UserResponse;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.mapper.UserMapper;
import com.backend.ecommercebackend.model.product.Category;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.model.user.User;
import com.backend.ecommercebackend.repository.order.OrderRepository;
import com.backend.ecommercebackend.repository.product.CategoryRepository;
import com.backend.ecommercebackend.repository.product.ProductRepository;
import com.backend.ecommercebackend.repository.user.UserRepository;
import com.backend.ecommercebackend.service.RecommendationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {

  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final UserMapper mapper;
  private final OrderRepository orderRepository;
  private final JwtService jwtService;
  private final Random random = new Random();


  @Override
  public List<Product> getRandomRecommendations(String token) {
    if (Boolean.TRUE.equals(jwtService.isTokenExpired(token))) {
      throw new ApplicationException(Exceptions.INVALID_TOKEN_EXCEPTION, "token expired");
    }
    String email = jwtService.extractUsername(token);
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND));

    boolean hasPurchased = orderRepository.existsByUserEmail(email);
    if (!hasPurchased) {
      throw new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION, "User has no valid purchases");
    }

    UserResponse response = mapper.entityToDto(user);
    response.setHasPurchased(true);

    List<Category> categories = categoryRepository.findAll();
    if (categories.isEmpty()) {
      throw new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION, "No categories found");
    }

    List<Product> recommendedProducts = new ArrayList<>();
    for (Category category : categories) {
      List<Product> products = productRepository.findByCategoryName(category.getCategoryName());
      if (!products.isEmpty()) {
        Product randomProduct = products.get(random.nextInt(products.size()));
        recommendedProducts.add(randomProduct);
      }
    }

    if (recommendedProducts.isEmpty()) {
      throw new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION, "No products found for recommendations");
    }

    return recommendedProducts;
  }
}

