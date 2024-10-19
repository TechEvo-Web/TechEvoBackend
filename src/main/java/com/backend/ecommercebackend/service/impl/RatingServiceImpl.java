package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.CommentRequest;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.model.product.Comment;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.repository.product.CommentRepository;
import com.backend.ecommercebackend.service.RatingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

  private final CommentRepository commentRepository;

  @Override
  public float getRating(CommentRequest commentRequest,Product product,Comment comment) {
    List<Comment> comments = commentRepository.findByProductId(commentRequest.getProductId());
    float ratingScore;
    if (commentRequest.getRating() == 0) {
      if (product.getRating() == 0) {
        ratingScore = Math.min(comments.size() + 1, 5);
    } else {
        ratingScore = product.getRating();
    }}
    else {
      ratingScore = getAverageRating(commentRequest, comments);
      product.setRating(ratingScore);
      comment.setRating(ratingScore);
    }
    return ratingScore;
    }

  @Override
  public float getAverageRating(CommentRequest commentRequest, List<Comment> comments) {
    float newRatingScore = commentRequest.getRating();
    if (newRatingScore < 0 || newRatingScore > 5) {
      throw new ApplicationException(Exceptions.INVALID_NUMBER_RANGE_EXCEPTION);
    }
    long numberOfRatings = comments.stream()
            .filter(c -> c.getRating() > 0)
            .count();
    float sumOfRatings = comments.stream()
            .map(Comment::getRating)
            .filter(rating -> rating > 0)
            .reduce(0f, Float::sum);
    sumOfRatings += newRatingScore;
    numberOfRatings += 1;
    float averageRating = numberOfRatings > 0 ? sumOfRatings / numberOfRatings : 0;
    return Math.min(Math.round(averageRating * 10) / 10.0f, 5);
  }
}
