package com.backend.ecommercebackend.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSpecificationRequest {
     int categoryId;
     String specificationName;
}
