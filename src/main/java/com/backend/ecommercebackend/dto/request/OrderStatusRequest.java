package com.backend.ecommercebackend.dto.request;

import com.backend.ecommercebackend.model.order.OrderStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusRequest {
     OrderStatus orderStatus;
}
