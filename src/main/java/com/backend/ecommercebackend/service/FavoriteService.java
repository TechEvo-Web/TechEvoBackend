package com.backend.ecommercebackend.service;


import com.backend.ecommercebackend.controller.FavoritesController.FavoritesResponse;
import com.backend.ecommercebackend.controller.FavoritesController.FavoritesRequest;
import java.util.List;

public interface FavoriteService {
  FavoritesResponse addFavorites(FavoritesRequest request, String token);

  List<FavoritesResponse> getFavorites(String token);

  void deleteFav(Long id, String token);
}
