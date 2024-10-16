package com.backend.ecommercebackend.dto.response;

import com.backend.ecommercebackend.dto.ProductSpecificationDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    int categoryId;
    String categoryName;
    List<ProductSpecificationDto> specifications;
}
