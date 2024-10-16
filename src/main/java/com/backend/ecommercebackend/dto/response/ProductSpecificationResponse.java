package com.backend.ecommercebackend.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSpecificationResponse {
    Long specificationId;
    String specificationName;
    int categoryId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
