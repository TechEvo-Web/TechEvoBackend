package com.backend.ecommercebackend.repository.product;

import com.backend.ecommercebackend.model.product.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Transactional
    @Query("""
            select c from Comment c where c.productId=:productId
            """)
    List<Comment>findByProductId(Long productId);

    @Transactional
    void deleteByProductId(Long productId);
}
