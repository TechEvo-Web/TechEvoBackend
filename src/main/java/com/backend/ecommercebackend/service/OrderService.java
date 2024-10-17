package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.model.order.OrderItem;

import java.util.List;

public interface OrderService {
    void processOrderItems(List<OrderItem> orderItems, Long orderId);
}
