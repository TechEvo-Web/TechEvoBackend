package com.backend.ecommercebackend.dto.request;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    String name;
    String modelNumber;
    double price;
    double discountPrice;
    String description;
    String categoryName;
    float rating;
    String usingPurpose;
    String whereUse;
    String look;
    Boolean isStock;
    int stockQuantity;
    Map<String,Object> specifications;
}
