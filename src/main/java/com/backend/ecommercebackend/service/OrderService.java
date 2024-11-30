package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.OrderRequest;
import com.backend.ecommercebackend.dto.request.OrderStatusRequest;
import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.model.product.Product;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Order processOrderItems(OrderRequest orderRequest,String token);
    Product getProductIdFromOrderItemId(Long orderItemId);
    List<Order> getOrdersByToken(String token);
    public void updateOrderStatus(Long orderId, OrderStatusRequest orderStatusRequest);
    Map<String, Long> getOrdersGroupedByStatus();
    Map<String, Long> findMonthlyData();
}
