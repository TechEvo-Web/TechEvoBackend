package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.OrderItemRequest;
import com.backend.ecommercebackend.dto.request.OrderRequest;
import com.backend.ecommercebackend.dto.request.OrderStatusRequest;
import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.model.order.OrderItem;
import com.backend.ecommercebackend.model.product.Product;

import java.util.List;
import java.util.Map;

public interface OrderService {
    List<Order> getOrders();
    Order processOrderItems(OrderRequest orderRequest,String token);
    Product getProductIdFromOrderItemId(Long orderItemId);
    List<Order> getOrdersByToken(String token);
    void updateOrderStatus(Long orderId, OrderStatusRequest orderStatusRequest);
//    Map<String, Long> findMonthlyData();
    void updateOrderStatusToImtina(Long orderId);
    Order updateOrderItem(Long orderId, Long itemId, OrderItem newItem);
    Order removeOrderItem(Long orderId, Long orderItemId);
    Order addOrderItem(Long orderId, OrderItemRequest newItem);
}
