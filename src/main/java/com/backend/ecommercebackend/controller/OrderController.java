package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.response.ProductResponse;
import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.model.order.OrderItem;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.repository.order.OrderItemRepository;
import com.backend.ecommercebackend.repository.order.OrderRepository;
import com.backend.ecommercebackend.repository.product.ProductRepository;
import com.backend.ecommercebackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    @PostMapping
    public ResponseEntity<Order> addOrderItems(@RequestBody Order order) {
        Order createdOrder = orderService.processOrderItems(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Void> deleteOrderItems(@PathVariable Long orderId) {
        orderRepository.deleteById(orderId);
        return ResponseEntity.noContent().build();
    }


@GetMapping ("/orderItem/{orderItemId}")
    public Product getOrderItemInfo(@PathVariable Long orderItemId){
        orderService.getProductIdFromOrderItemId(orderItemId);
        return orderService.getProductIdFromOrderItemId(orderItemId);
}
    @DeleteMapping("/orderItem/delete/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId) {
        orderItemRepository.deleteById(orderItemId);

        return ResponseEntity.noContent().build();
    }

}



