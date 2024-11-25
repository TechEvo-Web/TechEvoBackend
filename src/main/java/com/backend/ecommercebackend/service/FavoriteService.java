package com.backend.ecommercebackend.service;


import com.backend.ecommercebackend.controller.FavoritesController.FavoritesResponse;
import com.backend.ecommercebackend.controller.FavoritesController.FavoritesRequest;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import java.util.List;

public interface FavoriteService {
  FavoritesResponse addFavorites(FavoritesRequest request, String token);
  List<ProductResponse> getFavorites(String token);
  void deleteFav(Long id, String token);
}
