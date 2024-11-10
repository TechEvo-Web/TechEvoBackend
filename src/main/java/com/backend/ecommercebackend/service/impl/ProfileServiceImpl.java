package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.authentication.jwt.JwtService;
import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.repository.order.OrderRepository;
import com.backend.ecommercebackend.service.ProfileService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl  implements ProfileService {
    private final JwtService jwtService;
    private final OrderRepository orderRepository;

    @Override
    public List<Order> getOrdersWithToken(String token){
        String email = jwtService.extractUsername(token);
        return orderRepository.findByUserEmail(email);
    }
}
