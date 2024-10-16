package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.request.CommentRequest;
import com.backend.ecommercebackend.dto.response.CommentResponse;
import com.backend.ecommercebackend.model.product.Comment;
import com.backend.ecommercebackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    @GetMapping("/{productId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(service.getAllCommentsByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CommentRequest request) {
        final var createdComment = service.addComment(userDetails,request);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{commentId").build(createdComment.getCommentId());
        return ResponseEntity.created(location).body(createdComment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentRequest request) {
        final var updatedComment = service.updateComment(commentId,request);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{commentId}").build(updatedComment.getCommentId());
        return ResponseEntity.created(location).body(updatedComment);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        service.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
