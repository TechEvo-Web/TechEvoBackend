package com.backend.ecommercebackend.controller;


import com.backend.ecommercebackend.dto.request.BlogPostRequest;
import com.backend.ecommercebackend.model.blog.BlogPost;
import com.backend.ecommercebackend.service.BlogPostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/blog")
@RequiredArgsConstructor
@Slf4j
public class BlogPostController {
    private final BlogPostService service;

    @GetMapping("/getAll")
    @Operation(summary = "Get all blog posts")
    public ResponseEntity<List<BlogPost>> getAllBlogPosts() {
        return ResponseEntity.ok(service.getAllBlogPosts());
    }

    @GetMapping("/getAllByCategoryName")
    @Operation(summary = "Get blog posts by category name")
    public ResponseEntity<List<BlogPost>> getBlogPostsByCategory(@RequestParam String categoryName) {
        return ResponseEntity.ok(service.getBlogPostsByCategoryName(categoryName));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a blog post by id")
    public ResponseEntity<BlogPost> getBlogPostById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getBlogPostById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new blog post")
    public ResponseEntity<BlogPost> createBlogPost(@RequestPart(name = "request") BlogPostRequest request, @RequestParam("imageFile") List<MultipartFile> imageFiles) {
        final var createdBlogPost = service.addBlogPost(request, imageFiles);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").build(createdBlogPost.getId());
        return ResponseEntity.created(location).body(createdBlogPost);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a blog post by id")
    public ResponseEntity<BlogPost> updateBlogPost(@PathVariable Long id, @RequestPart(name = "request", required = false) BlogPostRequest request, @RequestParam(value = "imageFile", required = false) List<MultipartFile> imageFiles) throws IOException {
        final var updatedBlogPost = service.updateBlogPost(id, request, imageFiles);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}").build(updatedBlogPost.getId());
        return ResponseEntity.created(location).body(updatedBlogPost);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a blog post by id")
    public ResponseEntity<Void> deleteBlogPost(@PathVariable Long id) {
        service.deleteBlogPost(id);
        return ResponseEntity.noContent().build();
    }
}
