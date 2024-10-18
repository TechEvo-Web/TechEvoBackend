package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.model.order.OrderItem;
import com.backend.ecommercebackend.model.product.Product;

import java.util.List;

public interface OrderService {
    Order processOrderItems(List<OrderItem> orderItems,Order order);
    Product getProductIdFromOrderItemId(Long orderItemId);

}
