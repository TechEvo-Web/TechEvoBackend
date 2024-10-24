package com.backend.ecommercebackend.service.impl;


import com.backend.ecommercebackend.authentication.jwt.JwtService;
import com.backend.ecommercebackend.cache.service.RedisTokenService;
import com.backend.ecommercebackend.dto.request.OrderItemRequest;
import com.backend.ecommercebackend.dto.request.OrderRequest;
import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.model.order.OrderItem;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.model.user.User;
import com.backend.ecommercebackend.repository.order.OrderItemRepository;
import com.backend.ecommercebackend.repository.order.OrderRepository;
import com.backend.ecommercebackend.repository.product.ProductRepository;
import com.backend.ecommercebackend.repository.user.UserRepository;
import com.backend.ecommercebackend.service.OrderService;
import lombok.RequiredArgsConstructor;
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
    private final UserRepository userRepository;
    private final RedisTokenService redisTokenService;
    private final JwtService jwtService;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public Order processOrderItems(OrderRequest orderRequest,String token) {
        String email = jwtService.extractUsername(token);  

        Order addedOrder = new Order();
        addedOrder.setDeliveryType(orderRequest.getDeliveryType());
        addedOrder.setTotalPrice(orderRequest.getTotalPrice());
        addedOrder.setToken(token);

        userRepository.findByEmail(from);

        List<OrderItem> savedOrderItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(orderItemRequest.getProductId());
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setPrice(orderItemRequest.getPrice());
            orderItem.setProductUrl(orderItemRequest.getProductUrl() + orderItemRequest.getProductId());
            savedOrderItems.add(orderItem);
            orderItemRepository.save(orderItem);
        }

         addedOrder.setOrderItems(savedOrderItems);
        orderRepository.save(addedOrder);

         SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo("serxanbabayev614@gmail.com");
        message.setSubject("Yeni sifariş !");

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Umumi qiymet : " + addedOrder.getTotalPrice() + "\n");
        stringBuffer.append("Çatdirilma : " + addedOrder.getDeliveryType() + "\n");
        stringBuffer.append("Musteri E-Mail : " + email + "\n");
        stringBuffer.append("--------------------- \n");

        for (OrderItem oi : savedOrderItems) { // Entity'den mail içeriği oluştur
            stringBuffer.append("Mehsul ID : " + oi.getProductId() + "\n");
            stringBuffer.append("Mehsul Qiymeti : " + oi.getPrice() + "\n");
            stringBuffer.append("Mehsul Sayi : " + oi.getQuantity() + "\n");
            stringBuffer.append("Mehsul URL : " + oi.getProductUrl() + "\n");
            stringBuffer.append("--------------------- \n");
        }

        message.setText(stringBuffer.toString());
        mailSender.send(message);

        return addedOrder;
    }


    @Override
    public Product getProductIdFromOrderItemId(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).get();
        Long productId = orderItem.getProductId();
        Product product = productRepository.findById(productId).get();
        return product;
    }
}



