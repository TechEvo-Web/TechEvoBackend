package com.backend.ecommercebackend.repository.product;
import com.backend.ecommercebackend.model.product.ProductSpecification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecificationRepository extends JpaRepository<ProductSpecification, Long> {
    @Transactional
    void deleteByCategoryId(int categoryId);

    List<ProductSpecification> findByCategoryId(int categoryId);
}
