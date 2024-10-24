package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.OrderRequest;
import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.model.order.OrderItem;
import com.backend.ecommercebackend.model.product.Product;
import org.mapstruct.Mapper;

import java.util.List;

public interface OrderService {
    Order processOrderItems(OrderRequest orderRequest,String token);
    Product getProductIdFromOrderItemId(Long orderItemId);

}
