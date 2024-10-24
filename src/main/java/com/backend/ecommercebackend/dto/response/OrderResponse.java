package com.backend.ecommercebackend.dto.response;

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

public class OrderResponse {
    int totalPrice;
    String deliveryType;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    List<OrderItem> orderItems;
}
