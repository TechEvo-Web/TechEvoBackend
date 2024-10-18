package com.backend.ecommercebackend.controller;


import com.backend.ecommercebackend.dto.request.RatingRequest;
import com.backend.ecommercebackend.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/product/rate")
@RequiredArgsConstructor
public class RatingController {

  private final RatingService ratingService;

  @PostMapping
  public ResponseEntity<Float> rateProduct(@RequestBody RatingRequest request) {
    float ratingResponse = ratingService.addRating(request);
    return ResponseEntity.ok(ratingResponse);
  }
}
