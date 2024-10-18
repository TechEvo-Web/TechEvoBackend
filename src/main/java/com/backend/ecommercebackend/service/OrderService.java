package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.model.order.OrderItem;
import com.backend.ecommercebackend.model.product.Product;
import org.mapstruct.Mapper;

import java.util.List;

public interface OrderService {
    Order processOrderItems(Order order);
    Product getProductIdFromOrderItemId(Long orderItemId);

}
