package com.backend.ecommercebackend.mapper;

import com.backend.ecommercebackend.dto.request.AddressRequest;
import com.backend.ecommercebackend.dto.request.OrderRequest;
import com.backend.ecommercebackend.dto.request.UserDataRequest;
import com.backend.ecommercebackend.model.order.Address;
import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.model.order.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "address", target = "address")
    @Mapping(source = "userData", target = "userData")
    @Mapping(source = "deliveryType", target = "deliveryType")
    @Mapping(source = "totalPrice", target = "totalPrice")
    Order toOrder(OrderRequest orderRequest);

    UserData toUserData(UserDataRequest userDataRequest);

    Address toAddress(AddressRequest addressRequest);
}

