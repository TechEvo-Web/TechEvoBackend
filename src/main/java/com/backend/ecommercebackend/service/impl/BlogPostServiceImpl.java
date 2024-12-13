package com.backend.ecommercebackend.service.impl;


import com.backend.ecommercebackend.dto.request.BlogPostRequest;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.mapper.BlogPostMapper;
import com.backend.ecommercebackend.model.blog.BlogPost;
import com.backend.ecommercebackend.repository.blog.BlogPostRepository;
import com.backend.ecommercebackend.service.BlogPostService;
import com.backend.ecommercebackend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogPostServiceImpl implements BlogPostService {
    private final BlogPostMapper mapper;
    private final BlogPostRepository repository;
    private final FileStorageService fileStorageService;

    @Override
    public BlogPost addBlogPost(BlogPostRequest request, List<MultipartFile> imageFiles) {
        BlogPost blogPost = mapper.blogPostRequestToEntity(request);
        List<String> imageUrls = new ArrayList<>();
        addImage(imageFiles, blogPost, imageUrls);
        return repository.save(blogPost);
    }

    @Override
    public BlogPost updateBlogPost(Long id, BlogPostRequest request, List<MultipartFile> imageFiles) throws IOException {
        BlogPost blogPost = repository.findById(id).orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
        mapper.updateBlogPostFromRequest(request, blogPost);
        List<String> imageUrls = new ArrayList<>();
        if (imageFiles != null) {
            for (String imageUrl : blogPost.getImageUrl()) {
                fileStorageService.deleteFile(imageUrl);
            }
            blogPost.setImageUrl(imageUrls);
            addImage(imageFiles, blogPost, imageUrls);
        }
        return repository.save(blogPost);
    }

    @Override
    public BlogPost getBlogPostById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
    }

    @Override
    public List<BlogPost> getBlogPostsByCategoryName(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION, "Category name cannot be empty");
        }
        List<BlogPost> blogPosts = repository.findByCategoryName(categoryName);
        if (blogPosts.isEmpty()) {
            throw new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION, "No blog posts found or wrong category name");
        }
        return blogPosts;
    }

    @Override
    public List<BlogPost> getAllBlogPosts() {
        return repository.findAll();
    }

    @Override
    public void deleteBlogPost(Long id) {
        BlogPost blogPost = repository.findById(id).orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
        for (String imageUrl : blogPost.getImageUrl()) {
            if (imageUrl != null) {
                try {
                    fileStorageService.deleteFile(imageUrl);
                } catch (IOException e) {
                    log.error("Failed to delete image");
                }
            }
        }
        repository.deleteById(id);
    }

    private void addImage(List<MultipartFile> imageFiles, BlogPost blogPost, List<String> imageUrls) {
        for (MultipartFile imageFile : imageFiles) {
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    String url = fileStorageService.storeImages(imageFile, "blogPostImages");
                    imageUrls.add(url);
                    blogPost.setImageUrl(imageUrls);
                } catch (IOException e) {
                    throw new ApplicationException(Exceptions.IMAGE_STORAGE_EXCEPTION);
                }
            }
        }
    }
}
