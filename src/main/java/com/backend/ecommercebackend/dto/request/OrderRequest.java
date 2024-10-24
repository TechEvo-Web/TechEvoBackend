package com.backend.ecommercebackend.dto.request;

import com.backend.ecommercebackend.model.order.OrderItem;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    int totalPrice;
    String deliveryType;
    String token;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    List<OrderItemRequest> orderItems;
}
