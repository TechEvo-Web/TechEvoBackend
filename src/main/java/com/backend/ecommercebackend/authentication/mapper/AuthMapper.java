package com.backend.ecommercebackend.authentication.mapper;

import com.backend.ecommercebackend.authentication.dto.request.RegisterRequest;
import com.backend.ecommercebackend.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(request.getPassword()))")
    @Mapping(target ="role",ignore = true )
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    @Mapping(target = "id",ignore = true)
    User RegisterDtoToEntity(RegisterRequest request, PasswordEncoder passwordEncoder);
}
