package com.backend.ecommercebackend.mapper;

import com.backend.ecommercebackend.dto.request.CommentRequest;
import com.backend.ecommercebackend.dto.response.CommentResponse;
import com.backend.ecommercebackend.model.product.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentResponse CommentEntityToDto(Comment comment);
    Comment CommentDtoToComment(CommentRequest commentRequest);
    List<CommentResponse>CommentEntityListToDtoList(List<Comment> comments);
    Comment updateCommentEntityFromDto(CommentRequest commentRequest,@MappingTarget Comment comment);
}
