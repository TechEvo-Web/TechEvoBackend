package com.backend.ecommercebackend.mapper;

import com.backend.ecommercebackend.dto.request.UserRequest;
import com.backend.ecommercebackend.dto.response.UserResponse;
import com.backend.ecommercebackend.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    List<UserResponse> entityListToDtoList(List<User> usersList);
    UserResponse entityToDto(User user);
    User dtoToEntity(UserRequest userRequest);
    User updateEntityFromDto(UserRequest userRequest,@MappingTarget User user);
}
