package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.repository.order.OrderRepository;
import com.backend.ecommercebackend.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {
  private final OrderRepository orderRepository;
  private final ProfileService profileService;

  @Transactional
  @GetMapping("/getOrders")
  @Operation(summary = "İstifadəçinin sifarişlərini token göndərərək emailə görə tapmaq üçün endpoint")
  public List<Order> getOrders(@RequestHeader("Authorization") String token) {
    token = token.substring(7);
    List<Order> orders = profileService.getOrdersWithToken(token);
    return orders.isEmpty() ? new ArrayList<>() : orders;
  }
}
