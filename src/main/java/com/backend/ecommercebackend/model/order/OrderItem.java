package com.backend.ecommercebackend.model.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "order_items")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    int quantity;
    int price;
    Long productId;
    String productName;
    String productUrl="http://localhost:8081/api/v1/product/";
    int stockQuantity;
}