package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.CommentRequest;
import com.backend.ecommercebackend.dto.response.CommentResponse;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.exception.GlobalExceptionHandler;
import com.backend.ecommercebackend.mapper.CommentMapper;
import com.backend.ecommercebackend.model.product.Comment;
import com.backend.ecommercebackend.model.user.User;
import com.backend.ecommercebackend.repository.product.CommentRepository;
import com.backend.ecommercebackend.repository.user.UserRepository;
import com.backend.ecommercebackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final UserRepository userRepository;

    @Override
    public CommentResponse addComment(UserDetails userDetails,CommentRequest commentRequest) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()->new ApplicationException(Exceptions.USER_NOT_FOUND));
        String commentOwner = String.format("%s %s",user.getFirstName(),user.getLastName());
        Comment comment = mapper.CommentDtoToComment(commentRequest);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setCommentOwner(commentOwner);
        comment.setProfileImg(user.getProfileImg());
        repository.save(comment);
        return mapper.CommentEntityToDto(comment);
    }

    @Override
    public List<CommentResponse> getAllCommentsByProductId(Long productId) {
        List<Comment>comments = repository.findByProductId(productId);
        return mapper.CommentEntityListToDtoList(comments);
    }

    @Override
    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
        Comment comment = repository.findById(commentId).orElseThrow(()->new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
        Comment save = mapper.updateCommentEntityFromDto(commentRequest,comment);
        save.setUpdatedAt(LocalDateTime.now());
        repository.save(save);
        return mapper.CommentEntityToDto(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
     repository.deleteById(commentId);
    }
}
