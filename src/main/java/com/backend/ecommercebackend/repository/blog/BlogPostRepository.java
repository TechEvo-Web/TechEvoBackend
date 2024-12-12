package com.backend.ecommercebackend.repository.blog;


import com.backend.ecommercebackend.model.blog.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findByCategoryName(String categoryName);
}
