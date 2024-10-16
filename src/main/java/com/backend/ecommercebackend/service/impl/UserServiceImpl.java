package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.response.UserResponse;
import com.backend.ecommercebackend.dto.request.UserRequest;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.mapper.UserMapper;
import com.backend.ecommercebackend.model.user.User;
import com.backend.ecommercebackend.repository.user.UserRepository;
import com.backend.ecommercebackend.service.FileStorageService;
import com.backend.ecommercebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;
    private final UserRepository repository;
    private final FileStorageService storageService;

    @Override
    public List<UserResponse> getAllUsers() {
        return mapper.entityListToDtoList(repository.findAll());
    }


    @Override
    public UserResponse updateUser(Long id, UserRequest request, MultipartFile file) {
        User user = repository.findById(id).orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND));
        mapper.updateEntityFromDto(request, user);
        user.setUpdatedAt(LocalDateTime.now());
        try {
            storageService.deleteFile(user.getProfileImg());
            user.setProfileImg("");
            String url = storageService.storeImages(file, "profileImages");
            user.setProfileImg(url);
        } catch (IOException e) {
            throw new ApplicationException(Exceptions.IMAGE_STORAGE_EXCEPTION);

        }
        repository.save(user);
        return mapper.entityToDto(user);
    }

    @Override
    public UserResponse updateUserImg(Long id, MultipartFile file) {
        User user = repository.findById(id).orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND));
        user.setUpdatedAt(LocalDateTime.now());
        try{
            storageService.deleteFile(user.getProfileImg());
            user.setProfileImg("");
            String url = storageService.storeImages(file, "profileImages");
            user.setProfileImg(url);
        } catch (IOException e) {
            throw new ApplicationException(Exceptions.IMAGE_STORAGE_EXCEPTION);

        }
        repository.save(user);
        return mapper.entityToDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND));
        try {
            storageService.deleteFile(user.getProfileImg());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        repository.deleteById(id);
    }

    @Override
    public void delete(String email) {
        Optional<User> user = repository.findByEmail(email);

        if (user.isEmpty()) {
            throw new ApplicationException(Exceptions.USER_NOT_FOUND);
        }

        try {
            storageService.deleteFile(user.get().getProfileImg());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        repository.delete(user.get());
    }
}
