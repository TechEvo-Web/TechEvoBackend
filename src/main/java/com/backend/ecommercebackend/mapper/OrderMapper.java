package com.backend.ecommercebackend.mapper;

import com.backend.ecommercebackend.dto.request.AddressRequest;
import com.backend.ecommercebackend.dto.request.OrderRequest;
import com.backend.ecommercebackend.model.order.Address;
import com.backend.ecommercebackend.model.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "address", target = "address")
    @Mapping(source = "deliveryType", target = "deliveryType")
    @Mapping(source = "totalPrice", target = "totalPrice")
    Order toOrder(OrderRequest orderRequest);

    Address toAddress(AddressRequest addressRequest);
}

