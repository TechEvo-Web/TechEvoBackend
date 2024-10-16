package com.backend.ecommercebackend.repository.product;

import com.backend.ecommercebackend.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
 @Query("""
select p from Product p where p.categoryName=:categoryName
""")
 List<Product> findByCategoryName(String categoryName);
}
