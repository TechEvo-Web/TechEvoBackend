package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.CommentRequest;
import com.backend.ecommercebackend.dto.response.CommentResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CommentService {
    CommentResponse addComment(UserDetails userDetails,CommentRequest commentRequest);
    void deleteComment(Long commentId);
    List<CommentResponse> getAllCommentsByProductId(Long productId);
    CommentResponse updateComment(Long commentId, CommentRequest commentRequest);
}
