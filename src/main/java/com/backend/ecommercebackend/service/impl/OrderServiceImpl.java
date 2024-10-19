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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final JavaMailSender mailSender;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    @Value("${spring.mail.username}")
    private String from;
    @Override
    public Order processOrderItems(Order order) {

        Order addedOrder = new Order();
        addedOrder.setDeliveryType(order.getDeliveryType());
        addedOrder.setTotalPrice(order.getTotalPrice());

        List<OrderItem> savedOrderItems = new ArrayList<>();

        for (OrderItem oi : order.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(oi.getProductId());
            orderItem.setQuantity(oi.getQuantity());
            orderItem.setPrice(oi.getPrice());
            orderItem.setProductUrl(oi.getProductUrl() +oi.getProductId());
            savedOrderItems.add(orderItem);
            orderItemRepository.save(orderItem);
        }
        order.setOrderItems(savedOrderItems);
        orderRepository.save(order);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo("serxanbabayev614@gmail.com");
        message.setSubject("Yeni sifariş !");

        int totalPrice = order.getTotalPrice();
        String deliveryType = order.getDeliveryType();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Umumi qiymet : "+totalPrice +"\n" );
        stringBuffer.append("Çatdirilma : "+deliveryType +"\n" );
        stringBuffer.append("--------------------- \n");
        for (OrderItem oi : savedOrderItems) { //
            stringBuffer.append("Mehsul ID : "+oi.getProductId() +"\n" );
            stringBuffer.append("Mehsul Qiymeti : "+oi.getPrice() +"\n" );
            stringBuffer.append("Mehsul Sayi : "+oi.getQuantity() +"\n" );
            stringBuffer.append("Mehsul URL : "+oi.getProductUrl() +"\n" );
            stringBuffer.append("--------------------- \n");
        }


        message.setText(stringBuffer.toString());
        mailSender.send(message);
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



