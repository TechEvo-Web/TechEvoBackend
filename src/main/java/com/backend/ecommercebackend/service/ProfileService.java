package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.model.order.Order;

import java.util.List;

public interface ProfileService {
   List<Order> getOrdersWithToken(String token);
}
