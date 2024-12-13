package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.BlogPostRequest;
import com.backend.ecommercebackend.model.blog.BlogPost;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BlogPostService {
    BlogPost addBlogPost(BlogPostRequest request, List<MultipartFile> imageFile);

    BlogPost updateBlogPost(Long id, BlogPostRequest request, List<MultipartFile> imageFile) throws IOException;

    BlogPost getBlogPostById(Long id);

    List<BlogPost> getBlogPostsByCategoryName(String categoryName);

    List<BlogPost> getAllBlogPosts();

    void deleteBlogPost(Long id);
}
