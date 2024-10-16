package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.request.UserRequest;
import com.backend.ecommercebackend.dto.response.UserResponse;
import com.backend.ecommercebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RequestMapping("/api/v1/user/")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/allUsers")
    public List<UserResponse> getAllUsers() {
        return service.getAllUsers();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @ModelAttribute UserRequest userRequest, @RequestParam("profileImg") MultipartFile multipartFile) {
        final var updatedUser = service.updateUser(id, userRequest, multipartFile);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("{id}").build(updatedUser.getId());
        return ResponseEntity.created(location).body(updatedUser);
    }

    @PutMapping("/updateUserImg/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,@RequestParam("profileImg") MultipartFile multipartFile) {
        final var updatedUser = service.updateUserImg(id,multipartFile);
        final var location = ServletUriComponentsBuilder.fromCurrentContextPath().path("{id}").build(updatedUser.getId());
        return ResponseEntity.created(location).body(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        service.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        service.delete(email);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
