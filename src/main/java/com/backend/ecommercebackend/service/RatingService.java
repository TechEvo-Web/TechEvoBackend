package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.CommentRequest;
import com.backend.ecommercebackend.model.product.Comment;
import com.backend.ecommercebackend.model.product.Product;
import java.util.List;

public interface RatingService {

  float getAverageRating(CommentRequest commentRequest, List<Comment> comments);
  float getRating(CommentRequest commentRequest, Product product, Comment comment);
}
