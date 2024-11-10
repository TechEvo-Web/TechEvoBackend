package com.backend.ecommercebackend.mapper;
import com.backend.ecommercebackend.dto.request.AddressRequest;
import com.backend.ecommercebackend.model.order.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
 Address AddressRequestToEntity(AddressRequest request);
 Address UpdateAddressFromAddressRequest(@MappingTarget Address address, AddressRequest request);
}
