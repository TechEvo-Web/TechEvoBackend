package com.backend.ecommercebackend.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class OrderItemResponse {
    int quantity;
    int price;
    Long productId;
    String productUrl="https://techevo.az/product/";

}
