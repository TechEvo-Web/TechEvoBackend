package com.backend.ecommercebackend.repository.product;

import com.backend.ecommercebackend.model.product.Favorites;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorites,Long> {

  List<Favorites> findByUserId(Long userId);

  boolean existsByUserIdAndProductId(Long userId, Long productId);

  Optional<Favorites>  findByUserIdAndProductId(Long id, Long id1);
}
