package com.backend.ecommercebackend.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequest {
    Long productId;
    String comment;
    float rating;
}
