package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.service.FavoriteService;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product/favorites")
public class FavoritesController {

  private final FavoriteService favoriteService;

  @PostMapping
  @Operation(summary = "Məhsulu favorite-lərə əlavə etmək üçündür. Token header-dən, product id response-dan alınır")
  public ResponseEntity<FavoritesResponse> addFavorite(@RequestHeader("Authorization") String token,
                                                       @RequestBody FavoritesRequest request) {
    return ResponseEntity.ok(favoriteService.addFavorites(request,token));
  }

  @GetMapping
  public ResponseEntity<List<FavoritesResponse>> getFavorites(@RequestHeader("Authorization") String token) {
    return ResponseEntity.ok(favoriteService.getFavorites(token));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteFavorite(@RequestHeader("Authorization") String token, @PathVariable Long id) {
    favoriteService.deleteFav(id, token);
    return ResponseEntity.noContent().build();
  }

  @Builder
  public record FavoritesResponse(Long productId) {
  }

  @Builder
  public record FavoritesRequest(Long productId) {
  }

}
