package com.backend.ecommercebackend.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.util.List;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    String name;
    String modelNumber;
    double price;
    String description;
    float rating;
    String categoryName;
    List<Object> specifications;
}
