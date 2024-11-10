package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.response.UserResponse;
import com.backend.ecommercebackend.dto.request.UserRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();
    UserResponse updateUser(UserDetails userDetails, UserRequest request, MultipartFile file);
    void deleteUserById(Long id);
    void delete(String email);
    UserResponse getUser(UserDetails user);
}
