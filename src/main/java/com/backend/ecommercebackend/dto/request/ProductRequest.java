package com.backend.ecommercebackend.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.util.List;
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
    String specifications;
}
