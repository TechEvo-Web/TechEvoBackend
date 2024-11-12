package com.backend.ecommercebackend.service.impl;


import com.backend.ecommercebackend.authentication.jwt.JwtService;
import com.backend.ecommercebackend.controller.FavoritesController.FavoritesResponse;
import com.backend.ecommercebackend.controller.FavoritesController.FavoritesRequest;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.mapper.ProductMapper;
import com.backend.ecommercebackend.model.product.Favorites;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.model.user.User;
import com.backend.ecommercebackend.repository.product.FavoriteRepository;
import com.backend.ecommercebackend.repository.product.ProductRepository;
import com.backend.ecommercebackend.repository.user.UserRepository;
import com.backend.ecommercebackend.service.FavoriteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

  private final FavoriteRepository favoriteRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final JwtService jwtService;
  private final ProductMapper mapper;

  @Override
  public FavoritesResponse addFavorites(FavoritesRequest request, String token) {
    if (Boolean.TRUE.equals(jwtService.isTokenExpired(token))) {
      throw new ApplicationException(Exceptions.INVALID_TOKEN_EXCEPTION,"token expired");
    }

    String email = jwtService.extractUsername(token);
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND));

    Product product = productRepository.findById(request.productId())
            .orElseThrow(()-> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION,"Product not found"));

    if (favoriteRepository.existsByUserIdAndProductId(user.getId(), product.getId())) {
      throw new ApplicationException(Exceptions.ALREADY_EXISTS_EXCEPTION, "Product is already in favorites");
    }

    Favorites favorites = Favorites.builder()
            .userId(user.getId())
            .productId(product.getId())
            .build();

    favoriteRepository.save(favorites);

    return FavoritesResponse
             .builder()
             .productId(favorites.getProductId())
             .build();
  }

  @Override
  public List<ProductResponse> getFavorites(String token) {
    String email = jwtService.extractUsername(token);

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND));
    List<Favorites> favoritesList = favoriteRepository.findByUserId(user.getId());

    return favoritesList.stream()
            .map(favorite -> productRepository.findById(favorite.getProductId())
                    .map(product -> mapper.toProductResponse(product, true))
                    .orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION, "Product not found")))
            .toList();

  }

  @Override
  public void deleteFav(Long productId, String token) {
    if (Boolean.TRUE.equals(jwtService.isTokenExpired(token))) {
      throw new ApplicationException(Exceptions.INVALID_TOKEN_EXCEPTION,"token expired");
    }

    String email = jwtService.extractUsername(token);

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND));

    Product product = productRepository.findById(productId)
            .orElseThrow(()-> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION,"Product not found"));

    var favorite = favoriteRepository.findByUserIdAndProductId(user.getId(), product.getId())
            .orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION, "Product not found in user's favorites"));

    if (favoriteRepository.existsByUserIdAndProductId(user.getId(), product.getId())) {
        favoriteRepository.delete(favorite);
    }
  }
}
