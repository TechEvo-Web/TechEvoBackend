package com.backend.ecommercebackend.mapper;
import com.backend.ecommercebackend.dto.request.AddressRequest;
import com.backend.ecommercebackend.dto.response.AddressResponse;
import com.backend.ecommercebackend.model.order.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
 Address AddressRequestToEntity(AddressRequest request);
 AddressResponse EntityToAddressResponse(Address address);
 Address UpdateAddressFromAddressRequest(Address address,@MappingTarget AddressRequest request);
}
