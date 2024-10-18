package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.RatingRequest;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.model.product.Comment;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.repository.product.CommentRepository;
import com.backend.ecommercebackend.repository.product.ProductRepository;
import com.backend.ecommercebackend.service.RatingService;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class RatingServiceImpl implements RatingService {

  private final ProductRepository productRepository;
  private final CommentRepository repository;

  @Override
  public float addRating(RatingRequest ratingRequest) {
    log.info("User is adding a rating for product {}", ratingRequest.getProductId());

    Product product = productRepository.findById(ratingRequest.getProductId())
            .orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));

    List<Comment> comments = repository.findByProductId(ratingRequest.getProductId());
    int commentCount = comments.size();

    float newRatingScore = ratingRequest.getRatingScore();
    List<Float> existingRatings = new ArrayList<>();

    if (product.getRating() > 0) {
      existingRatings.add(product.getRating());
    }

    if (newRatingScore > 0) {
      existingRatings.add(newRatingScore);
      float averageRating = (float) existingRatings.stream()
              .mapToDouble(Float::doubleValue)
              .average()
              .orElse(1.0);

      DecimalFormat df = new DecimalFormat("#.#");
      averageRating = Float.parseFloat(df.format(averageRating));

      product.setRating(averageRating);
      productRepository.save(product);
      log.info("New average rating for product {}: {}", product.getId(), averageRating);
      return averageRating;
    }
    else if (product.getRating() == 0 && commentCount > 0)
    {
      return Math.min(commentCount, 5);
    }
    else {
      return 1.0F;
    }
  }
}
