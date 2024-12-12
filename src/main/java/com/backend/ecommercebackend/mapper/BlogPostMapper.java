package com.backend.ecommercebackend.mapper;


import com.backend.ecommercebackend.dto.request.BlogPostRequest;
import com.backend.ecommercebackend.dto.response.BlogPostResponse;
import com.backend.ecommercebackend.model.blog.BlogPost;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BlogPostMapper {
    BlogPost blogPostRequestToEntity(BlogPostRequest request);

    BlogPostResponse blogPostToResponse(BlogPost blogPost);

    void updateBlogPostFromRequest(BlogPostRequest request, @MappingTarget BlogPost blogPost);
}
