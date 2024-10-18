package com.backend.ecommercebackend.service;


import com.backend.ecommercebackend.dto.request.RatingRequest;

public interface RatingService {

  float addRating(RatingRequest ratingRequest);
}
