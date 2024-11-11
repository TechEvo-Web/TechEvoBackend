package com.backend.ecommercebackend.service.impl;


import com.backend.ecommercebackend.authentication.jwt.JwtService;
import com.backend.ecommercebackend.cache.service.RedisTokenService;
import com.backend.ecommercebackend.dto.request.AddressRequest;
import com.backend.ecommercebackend.dto.request.OrderItemRequest;
import com.backend.ecommercebackend.dto.request.OrderRequest;
import com.backend.ecommercebackend.model.order.Address;
import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.model.order.OrderItem;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.model.user.User;
import com.backend.ecommercebackend.repository.order.OrderItemRepository;
import com.backend.ecommercebackend.repository.order.OrderRepository;
import com.backend.ecommercebackend.repository.product.ProductRepository;
import com.backend.ecommercebackend.repository.user.UserRepository;
import com.backend.ecommercebackend.service.OrderService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Order processOrderItems(OrderRequest orderRequest, String token) {
        String email = jwtService.extractUsername(token);


         Address address = new Address();
        address.setStreet(orderRequest.getAddress().getStreet());
        address.setCity(orderRequest.getAddress().getCity());
        address.setBuilding(orderRequest.getAddress().getBuilding());
        address.setArea(orderRequest.getAddress().getArea());

         Order addedOrder = new Order();
        addedOrder.setDeliveryType(orderRequest.getDeliveryType());
        addedOrder.setTotalPrice(orderRequest.getTotalPrice());
        addedOrder.setAddress(address);
        addedOrder.setUserEmail(email);



        List<OrderItem> savedOrderItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(orderItemRequest.getProductId());
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setPrice(orderItemRequest.getPrice());
            orderItem.setProductUrl(orderItem.getProductUrl() + orderItemRequest.getProductId());
            savedOrderItems.add(orderItem);
            orderItemRepository.save(orderItem);
        }

         addedOrder.setOrderItems(savedOrderItems);
        orderRepository.save(addedOrder);

        String htmlContent = "<html><body style='font-family: Arial, sans-serif; background-color: #f8f8f8; padding: 20px;'>" +
                "<div style='background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);'>" +
                "<h1 style='text-align: center; color: #333;'>Yeni Sifariş Bildirişi</h1>" +
                "<hr style='border: 1px solid #e0e0e0;'>" +

                "<p style='font-size: 16px; color: #555;'><strong>Ümumi Qiymət:</strong> <span style='color: #d32f2f;'>" + addedOrder.getTotalPrice() + " AZN</span></p>" +
                "<p style='font-size: 16px; color: #555;'><strong>Çatdırılma Seçimi:</strong> " + addedOrder.getDeliveryType() + "</p>" +
                "<p style='font-size: 16px; color: #555;'><strong>Müştəri E-mail:</strong> " + email + "</p>" +

                "<h2 style='font-size: 18px; color: #333;'>Ünvan Bilgiləri</h2>" +
                "<table style='width: 100%; border-collapse: collapse; margin-top: 10px;'>" +
                "<tr><td style='padding: 8px; border: 1px solid #ddd;'><strong>Bölgə:</strong></td><td style='padding: 8px; border: 1px solid #ddd;'>" + addedOrder.getAddress().getArea() + "</td></tr>" +
                "<tr><td style='padding: 8px; border: 1px solid #ddd;'><strong>Şəhər:</strong></td><td style='padding: 8px; border: 1px solid #ddd;'>" + addedOrder.getAddress().getCity() + "</td></tr>" +
                "<tr><td style='padding: 8px; border: 1px solid #ddd;'><strong>Küçə:</strong></td><td style='padding: 8px; border: 1px solid #ddd;'>" + addedOrder.getAddress().getStreet() + "</td></tr>" +
                "<tr><td style='padding: 8px; border: 1px solid #ddd;'><strong>Bina:</strong></td><td style='padding: 8px; border: 1px solid #ddd;'>" + addedOrder.getAddress().getBuilding() + "</td></tr>" +
                "</table>" +

                "<h2 style='font-size: 18px; color: #333; margin-top: 20px;'>Sifariş edilən məhsullar</h2>";

         for (OrderItem oi : savedOrderItems) {
            htmlContent += "<div style='background-color: #f1f1f1; padding: 10px; margin-top: 10px; border-radius: 8px;'>" +
                    "<p style='font-size: 16px;'><strong>Məhsul ID:</strong> " + oi.getProductId() + "</p>" +
                    "<p style='font-size: 16px;'><strong>Məhsul Qiyməti:</strong> " + oi.getPrice() + " AZN</p>" +
                    "<p style='font-size: 16px;'><strong>Məhsul Sayı:</strong> " + oi.getQuantity() + "</p>" +
                    "<p style='font-size: 16px;'><strong>Məhsul URL:</strong> <a href='" + oi.getProductUrl() + "' style='color: #1976D2; text-decoration: none;'>Məhsul URL</a></p>" +
                    "</div>";
        }




        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);



            helper.setTo("serxanbabayev614@gmail.com");
            helper.setSubject("Yeni Sifariş Bildişi");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return addedOrder;
    }




    @Override
    public Product getProductIdFromOrderItemId(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).get();
        Long productId = orderItem.getProductId();
        Product product = productRepository.findById(productId).get();
        return product;
    }
    @Override
    public List<Order>getOrdersByToken(String token) {
        String email = jwtService.extractUsername(token);
        List<Order> orders=orderRepository.findByUserEmail(email);
        return orders;
    }
}



