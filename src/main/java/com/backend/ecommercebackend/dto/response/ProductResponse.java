package com.backend.ecommercebackend.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Long id;
    String name;
    String modelNumber;
    String description;
    double price;
    float rating;
    List<String>imageUrl;
    String categoryName;
    List<Object> specifications;
}
