package com.backend.ecommercebackend.service.impl;


import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.model.order.OrderItem;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.repository.order.OrderItemRepository;
import com.backend.ecommercebackend.repository.order.OrderRepository;
import com.backend.ecommercebackend.repository.product.ProductRepository;
import com.backend.ecommercebackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final  OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public Order processOrderItems(List<OrderItem> orderItems,Order order) {

Order addedOrder = new Order();
addedOrder.setDeliveryType(order.getDeliveryType());
addedOrder.setTotalPrice(order.getTotalPrice());



        List<OrderItem> savedOrderItems = new ArrayList<>();

        for (OrderItem oi : orderItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(oi.getProductId());
            orderItem.setQuantity(oi.getQuantity());
            orderItem.setPrice(oi.getPrice());

            savedOrderItems.add(orderItem);


            orderItemRepository.save(orderItem);
        }


        order.setOrderItems(savedOrderItems);



        orderRepository.save(order);
        return order;
    }
    @Override
public Product getProductIdFromOrderItemId(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).get();
        Long productId = orderItem.getProductId();
        Product product = productRepository.findById(productId).get();
        return product;
    }
}



