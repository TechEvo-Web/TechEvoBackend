package com.backend.ecommercebackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatingRequest {
  private Long productId;
  private float ratingScore;

}
