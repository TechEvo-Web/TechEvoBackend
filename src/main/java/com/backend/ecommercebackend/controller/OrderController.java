package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.model.order.OrderItem;
import com.backend.ecommercebackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService; // final olarak tanımlandı

    @PostMapping("/{orderId}")
    public ResponseEntity<String> addOrderItems(@PathVariable Long orderId,
                                                @RequestBody List<OrderItem> orderItems) {

        orderService.processOrderItems(orderItems, orderId);
        return ResponseEntity.ok("Order items added successfully!");
    }


}
