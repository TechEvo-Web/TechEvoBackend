package com.backend.ecommercebackend.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemRequest {
    int quantity;
    int price;
    Long productId;
    String productUrl="http://localhost:8081/api/v1/product/";

}
