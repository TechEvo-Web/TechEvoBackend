package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.authentication.jwt.JwtService;
import com.backend.ecommercebackend.dto.request.OrderRequest;
import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.repository.order.OrderItemRepository;
import com.backend.ecommercebackend.repository.order.OrderRepository;
import com.backend.ecommercebackend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final JwtService jwtService;


    @PostMapping
    @Operation(summary = "İstifadəçinin sifarişlərini əlavə etmək üçün endpoint")
    public ResponseEntity<Order> addOrderItems(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String token) {
        token = token.substring(7);
        Order createdOrder = orderService.processOrderItems(orderRequest, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @DeleteMapping("/delete/{orderId}")
    @Operation(summary = "İstifadəçi sifarişlərini orderİd silmək üçün endpoint")
    public ResponseEntity<Void> deleteOrderItems(@PathVariable Long orderId) {
        orderRepository.deleteById(orderId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/orderItem/{orderItemId}")
    @Operation(summary = "OrderItem-i Product kimi qaytarmaq ucun endpoint")
    public Product getOrderItemInfo(@PathVariable Long orderItemId) {
        orderService.getProductIdFromOrderItemId(orderItemId);
        return orderService.getProductIdFromOrderItemId(orderItemId);
    }

    @GetMapping()
    @Operation(summary = "Token gondererek userin Orderlarini qaytarmaq ucun endpoint")
    public List<Order> getOrders(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        return orderService.getOrdersByToken(token);
    }
    @GetMapping("/{orderId}")
    @Operation(summary = "Order'ı id'ye göre tapmaq ucun endpoint")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String token) {
        token = token.substring(7);
        String userEmail = jwtService.extractUsername(token);

        return orderRepository.findById(orderId)
                .map(order -> {
                    if (order.getUserEmail().equals(userEmail)) {
                         return ResponseEntity.ok(order);
                    } else {
                         return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                    }
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/orderItem/delete/{orderItemId}")
    @Operation(summary = "İstifadəçi sifarişlərini orderİtemİd ilə silmək üçün endpoint")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId) {
        orderItemRepository.deleteById(orderItemId);

        return ResponseEntity.noContent().build();
    }

}



