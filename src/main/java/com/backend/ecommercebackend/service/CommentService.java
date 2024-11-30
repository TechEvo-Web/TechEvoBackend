package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.CommentRequest;
import com.backend.ecommercebackend.model.product.Comment;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CommentService {
    List<Comment>getAllComments();
    Comment addComment(UserDetails userDetails, CommentRequest commentRequest);
    void deleteComment(Long commentId);
    List<Comment> getAllCommentsByProductId(Long productId);
    Comment updateComment(Long commentId, CommentRequest commentRequest);
}
