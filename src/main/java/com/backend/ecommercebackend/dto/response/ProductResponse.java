package com.backend.ecommercebackend.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Long id;
    String name;
    String modelNumber;
    String description;
    double price;
    double discountPrice;
    float rating;
    List<String>imageUrl;
    String categoryName;
    Map<String,Object> specifications;
}
