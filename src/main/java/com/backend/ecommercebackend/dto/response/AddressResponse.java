package com.backend.ecommercebackend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {
    Long id;
    String street;
    String city;
    String building;
    String area;
    Long orderId;
}
