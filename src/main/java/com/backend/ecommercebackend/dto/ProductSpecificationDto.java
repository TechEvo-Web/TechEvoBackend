package com.backend.ecommercebackend.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSpecificationDto {
    Long specificationId;
    String specificationName;
}
