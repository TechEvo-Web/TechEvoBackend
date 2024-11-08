package com.backend.ecommercebackend.mapper;

import com.backend.ecommercebackend.dto.request.CommentRequest;
import com.backend.ecommercebackend.model.product.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment CommentDtoToComment(CommentRequest commentRequest);
    Comment updateCommentEntityFromDto(CommentRequest commentRequest,@MappingTarget Comment comment);
}
