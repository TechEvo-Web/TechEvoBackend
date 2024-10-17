package com.backend.ecommercebackend.service.impl;


import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.model.order.OrderItem;
import com.backend.ecommercebackend.repository.order.OrderItemRepository;
import com.backend.ecommercebackend.repository.order.OrderRepository;
import com.backend.ecommercebackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final  OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    @Override
    public void processOrderItems(List<OrderItem> orderItems, Long orderId) {

        Order order = new Order();
        order.setOrderId(orderId);
        order.setDeliveryType("Yes");

        int totalPrice = 0;


        List<OrderItem> savedOrderItems = new ArrayList<>();

        for (OrderItem oi : orderItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(oi.getProductId());
            orderItem.setQuantity(oi.getQuantity());
            orderItem.setPrice(oi.getPrice());


            totalPrice += oi.getPrice() * oi.getQuantity();


            savedOrderItems.add(orderItem);


            orderItemRepository.save(orderItem);
        }


        order.setOrderItems(savedOrderItems);
        order.setTotalPrice(totalPrice);


        orderRepository.save(order);
    }

}

