package com.backend.ecommercebackend.service.impl;


import com.backend.ecommercebackend.authentication.jwt.JwtService;
import com.backend.ecommercebackend.dto.request.OrderItemRequest;
import com.backend.ecommercebackend.dto.request.OrderRequest;
import com.backend.ecommercebackend.dto.request.OrderStatusRequest;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.mapper.OrderMapper;
import com.backend.ecommercebackend.model.order.*;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.repository.order.OrderItemRepository;
import com.backend.ecommercebackend.repository.order.OrderRepository;
import com.backend.ecommercebackend.repository.product.ProductRepository;
import com.backend.ecommercebackend.repository.user.UserRepository;
import com.backend.ecommercebackend.service.OrderService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
    private final JwtService jwtService;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public Order processOrderItems(OrderRequest orderRequest, String token) {

        for (OrderItemRequest orderItem : orderRequest.getOrderItems()) {
            int stockChecker=orderItem.getQuantity();
            Optional<Product> myProduct=productRepository.findById(orderItem.getProductId());
            if (stockChecker>myProduct.get().getStockQuantity()){
                throw new IllegalArgumentException("Not enough stock for product: " + myProduct.get().getName());

            }
        }
        String email = jwtService.extractUsername(token);
        LocalDate now = LocalDate.now();
        UserData userData=new UserData();
        userData.setPhoneNumber(orderRequest.getUserData().getPhoneNumber());
        userData.setAdditionalInfo(orderRequest.getUserData().getAdditionalInfo());
        userData.setName(userRepository.findByEmail(email).get().getFirstName());
        userData.setSurname(userRepository.findByEmail(email).get().getLastName());
        Order addedOrder = OrderMapper.INSTANCE.toOrder(orderRequest);
        Address address = OrderMapper.INSTANCE.toAddress(orderRequest.getAddress());
         addedOrder.setAddress(address);
        addedOrder.setUserEmail(email);
        addedOrder.setUserData(userData);
        addedOrder.setOrderStatus(OrderStatus.Pending);
        List<OrderItem> savedOrderItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
            int newStock=0;
            Product currentProduct=productRepository.findById(orderItemRequest.getProductId()).get();
            newStock=currentProduct.getStockQuantity()-orderItemRequest.getQuantity();
            currentProduct.setStockQuantity(newStock);
            productRepository.save(currentProduct);
             OrderItem orderItem = new OrderItem();
            orderItem.setProductName(productRepository.findById(orderItemRequest.getProductId()).get().getName());
            orderItem.setProductId(orderItemRequest.getProductId());
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setPrice(orderItemRequest.getPrice());
            orderItem.setStockQuantity(currentProduct.getStockQuantity());
            orderItem.setProductUrl(orderItem.getProductUrl() + orderItemRequest.getProductId());
            savedOrderItems.add(orderItem);
            orderItemRepository.save(orderItem);
        }

        addedOrder.setOrderItems(savedOrderItems);
        orderRepository.save(addedOrder);

        String adminHtmlContent = "<html><body style='font-family: Arial, sans-serif; background-color: #f8f8f8; padding: 20px;'>" +
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
                "</table>" + "<h2 style='font-size: 18px; color: #333; margin-top: 20px;'>Sifariş edilən məhsullar</h2>";


        String userHtmlContent = "<html><body style='font-family: Arial, sans-serif; background-color: #f8f8f8; padding: 20px;'>" +
                "<div style='background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);'>" +
                "<h1 style='text-align: center; color: #1976D2;'>🎉 Salam, hörmətli müştəri! 🎉</h1>" +
                "<p style='font-size: 16px; color: #555; text-align: center;'>Bizi seçdiyiniz üçün təşəkkür edirik! 🙏 Sifarişinizi aldıq və ən qısa müddətdə əməkdaşlarımız sizinlə əlaqə saxlayacaq. 😊</p>" +
                "<p style='font-size: 16px; color: #555; text-align: center;'>Sifariş nömrəniz: <strong style='color: #d32f2f;'>" + addedOrder.getOrderId() + "</strong></p>" +
                "<div style='margin-top: 20px;'>" +
                "<p style='font-size: 16px; color: #555;'><strong>💸 Ümumi Qiymət:</strong> <span style='color: #d32f2f;'>" + addedOrder.getTotalPrice() + " AZN</span></p>" +
                "<p style='font-size: 16px; color: #555;'><strong>🚚 Çatdırılma Seçimi:</strong> " + addedOrder.getDeliveryType() + "</p>" +

                "</div>" +

                "<h2 style='font-size: 18px; color: #1976D2; margin-top: 30px;'>🏠 Ünvan Bilgiləri</h2>" +
                "<div style='background-color: #f9f9f9; padding: 10px; border: 1px solid #ddd; border-radius: 8px; margin-top: 10px;'>" +
                "<p style='font-size: 16px;'><strong>📍 Bölgə:</strong> " + addedOrder.getAddress().getArea() + "</p>" +
                "<p style='font-size: 16px;'><strong>🏙️ Şəhər:</strong> " + addedOrder.getAddress().getCity() + "</p>" +
                "<p style='font-size: 16px;'><strong>🛣️ Küçə:</strong> " + addedOrder.getAddress().getStreet() + "</p>" +
                "<p style='font-size: 16px;'><strong>🏢 Bina:</strong> " + addedOrder.getAddress().getBuilding() + "</p>" +
                "</div>" +

                "<h2 style='font-size: 18px; color: #1976D2; margin-top: 30px;'>🛍️ Sifariş Edilən Məhsullar</h2>";

        for (OrderItem oi : savedOrderItems) {
            Product product = productRepository.findById(oi.getProductId()).orElse(null);
            if (product != null) {
                userHtmlContent += "<div style='background-color: #f1f1f1; padding: 10px; margin-top: 10px; border-radius: 8px; border: 1px solid #ddd;'>" +
                        "<p style='font-size: 16px;'><strong>📦 Məhsul Adı:</strong> " + product.getName() + "</p>" +
                        "<p style='font-size: 16px;'><strong>💵 Məhsul Qiyməti:</strong> " + oi.getPrice() + " AZN</p>" +
                        "<p style='font-size: 16px;'><strong>🔢 Məhsul Sayı:</strong> " + oi.getQuantity() + "</p>" +
                        "<p style='font-size: 16px;'><strong>🔗 Məhsul URL:</strong> <a href='" + oi.getProductUrl() + "' style='color: #1976D2; text-decoration: none;'>Məhsul Detalları</a></p>" +
                        "</div>";
            } else {
                userHtmlContent += "<div style='background-color: #f1f1f1; padding: 10px; margin-top: 10px; border-radius: 8px; border: 1px solid #ddd;'>" +
                        "<p style='font-size: 16px; color: #d32f2f;'>⚠️ Məhsul tapılmadı (ID: " + oi.getProductId() + ")</p>" +
                        "</div>";
            }
        }

        userHtmlContent += "<p style='font-size: 14px; color: #777; text-align: center; margin-top: 30px;'>Gözəl alışverişlər arzu edirik! 🛍️</p>" +
                "<p style='font-size: 14px; color: #777; text-align: center;'>Əgər hər hansı bir sualınız varsa, bizimlə əlaqə saxlamaqdan çəkinməyin. 📞+994 70 911 36 35</p>" +
                "</div></body></html>";


        for (OrderItem oi : savedOrderItems) {
            Product product = productRepository.findById(oi.getProductId()).orElse(null);
            if (product != null) {
                userHtmlContent += "<div style='background-color: #f1f1f1; padding: 10px; margin-top: 10px; border-radius: 8px; border: 1px solid #ddd;'>" +
                        "<p style='font-size: 16px;'><strong>Məhsul Adı:</strong> " + product.getName() + "</p>" +
                        "<p style='font-size: 16px;'><strong>Məhsul Qiyməti:</strong> " + oi.getPrice() + " AZN</p>" +
                        "<p style='font-size: 16px;'><strong>Məhsul Sayı:</strong> " + oi.getQuantity() + "</p>" +
                        "<p style='font-size: 16px;'><strong>Məhsul URL:</strong> <a href='" + oi.getProductUrl() + "' style='color: #1976D2; text-decoration: none;'>Məhsul Detalları</a></p>" +
                        "</div>";
            } else {
                userHtmlContent += "<div style='background-color: #f1f1f1; padding: 10px; margin-top: 10px; border-radius: 8px; border: 1px solid #ddd;'>" +
                        "<p style='font-size: 16px; color: #d32f2f;'>Məhsul tapılmadı (ID: " + oi.getProductId() + ")</p>" +
                        "</div>";
            }
        }

        userHtmlContent += "<div style='margin-top: 30px; text-align: center;'>" +
                "<a href='https://techevo.com/api/v1/order" + addedOrder.getOrderId() + "' style='padding: 10px 20px; background-color: #1976D2; color: #fff; text-decoration: none; border-radius: 5px; font-size: 16px;'>Sifariş Detallarını Gör</a>" +
                "</div>" +
                "</div></body></html>";


        for (OrderItem oi : savedOrderItems) {
            adminHtmlContent += "<div style='background-color: #f1f1f1; padding: 10px; margin-top: 10px; border-radius: 8px;'>" +
                    "<p style='font-size: 16px;'><strong>Məhsul ID:</strong> " + oi.getProductId() + "</p>" +
                    "<p style='font-size: 16px;'><strong>Məhsul Qiyməti:</strong> " + oi.getPrice() + " AZN</p>" +
                    "<p style='font-size: 16px;'><strong>Məhsul Sayı:</strong> " + oi.getQuantity() + "</p>" +
                    "<p style='font-size: 16px;'><strong>Məhsul URL:</strong> <a href='" + oi.getProductUrl() + "' style='color: #1976D2; text-decoration: none;'>Məhsul URL</a></p>" +
                    "</div>";
        }

        try {
            MimeMessage messageToAdmin = mailSender.createMimeMessage();
            MimeMessageHelper adminHelper = new MimeMessageHelper(messageToAdmin, true);
            adminHelper.setTo("serxanbabayev614@gmail.com");
            adminHelper.setSubject("Yeni Sifariş Bildirişi");
            adminHelper.setText(adminHtmlContent, true);
            mailSender.send(messageToAdmin);

            MimeMessage messageToUser = mailSender.createMimeMessage();
            MimeMessageHelper userHelper = new MimeMessageHelper(messageToUser, true);
            userHelper.setTo(email);
            userHelper.setSubject("Sifarişini qəbul etdik ✅");
            userHelper.setText(userHtmlContent, true);
            mailSender.send(messageToUser);
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
    public List<Order> getOrdersByToken(String token) {
        String email = jwtService.extractUsername(token);
        List<Order> orders = orderRepository.findByUserEmail(email);
        return orders;
    }


    @Override
    public List<Order> getOrders() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                Optional<Product> productOpt = productRepository.findById(orderItem.getProductId());
                if (productOpt.isPresent()) {
                    orderItem.setProductName(productOpt.get().getName());
                } else {
                    throw new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION);
                }
            }
        }
        return orders;
    }


    @Override
    public void updateOrderStatus(Long orderId, OrderStatusRequest orderStatusRequest) {
        // Order'ı bul
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        // Yeni sipariş durumunu al
        OrderStatus newStatus = orderStatusRequest.getOrderStatus();

        // Eğer durum "Canceled" ise stokları güncelle
        if (newStatus == OrderStatus.Canceled) {
            for (OrderItem item : order.getOrderItems()) {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + item.getProductId()));

                // Stoku geri yükle
                product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        }

        // Sipariş durumunu güncelle
        order.setOrderStatus(newStatus);
        orderRepository.save(order);
    }


    @Override
    public void updateOrderStatusToImtina(Long orderId) {
        // Order'ı bul
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        order.setOrderStatus(OrderStatus.Canceled);
        // Order içindeki her bir OrderItem'in stoğunu geri ekle
        for (OrderItem item : order.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + item.getProductId()));

            // Stoku geri ekle
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
            orderRepository.save(order);

        }

        // Sipariş durumunu "Canceled" olarak güncelle
        order.setOrderStatus(OrderStatus.Canceled);
        orderRepository.save(order);
    }


    public Order updateOrderItem(Long orderId, Long itemId, OrderItemRequest newItem) {
        // Order'ı bul
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Güncellenmesi gereken OrderItem'ı bul
        OrderItem existingItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));

        // Mevcut ürünün stok bilgisini güncelle
        Product existingProduct = productRepository.findById(existingItem.getProductId())
                .orElseThrow(() -> new RuntimeException("Existing product not found"));
        existingProduct.setStockQuantity(existingProduct.getStockQuantity() + existingItem.getQuantity());
        productRepository.save(existingProduct);

        // Yeni ürünün stok bilgisini güncelle
        Product newProduct = productRepository.findById(newItem.getProductId())
                .orElseThrow(() -> new RuntimeException("New product not found"));
        if (newProduct.getStockQuantity() < newItem.getQuantity()) {
            throw new RuntimeException("Not enough stock for product ID: " + newItem.getProductId());
        }
        newProduct.setStockQuantity(newProduct.getStockQuantity() - newItem.getQuantity());
        productRepository.save(newProduct);

        // OrderItem güncellemesi
        existingItem.setQuantity(newItem.getQuantity());
        existingItem.setProductId(newItem.getProductId());
        existingItem.setProductName(newProduct.getName());
        existingItem.setPrice(newItem.getPrice());
        orderItemRepository.save(existingItem);

        // Order toplam miktar ve fiyatı güncelle
         int totalPrice = order.getOrderItems().stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum();
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
        return order;
    }



    public Order removeOrderItem(Long orderId, Long orderItemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));

        Product product = productRepository.findById(orderItem.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStockQuantity(product.getStockQuantity() + orderItem.getQuantity());
        productRepository.save(product);

        order.getOrderItems().removeIf(item -> item.getId().equals(orderItemId));
        orderItemRepository.deleteById(orderItemId);

        if (order.getOrderItems().isEmpty()) {
            orderRepository.delete(order);
        } else {
            orderRepository.save(order);
        }

        return order;
    }


    @Override
    public Order addOrderItem(Long orderId, OrderItemRequest orderItemRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Product product = productRepository.findById(orderItemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < orderItemRequest.getQuantity()) {
            throw new RuntimeException("Not enough stock available");
        }

        product.setStockQuantity(product.getStockQuantity() - orderItemRequest.getQuantity());
        productRepository.save(product);

        OrderItem newItem = new OrderItem();
        newItem.setQuantity(orderItemRequest.getQuantity());
        newItem.setPrice(orderItemRequest.getPrice());
        newItem.setProductId(orderItemRequest.getProductId());
        newItem.setProductName(product.getName());
        newItem.setProductUrl("http://localhost:8081/api/v1/product/" + orderItemRequest.getProductId());
        orderItemRepository.save(newItem);

        order.getOrderItems().add(newItem);
        int totalPrice = order.getOrderItems().stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum();
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
        return order;
    }


}





