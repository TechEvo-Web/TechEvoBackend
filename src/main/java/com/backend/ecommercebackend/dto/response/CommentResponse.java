package com.backend.ecommercebackend.dto.response;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    Long commentId;
    String comment;
    String commentOwner;
    String profileImg;
    float rating;
    Long productId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
