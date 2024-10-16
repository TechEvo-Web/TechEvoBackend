package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.response.UserResponse;
import com.backend.ecommercebackend.dto.request.UserRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();
    UserResponse updateUser(Long id, UserRequest request, MultipartFile file);
    UserResponse updateUserImg(Long id, MultipartFile file);
    void deleteUserById(Long id);
    void delete(String email);

}
