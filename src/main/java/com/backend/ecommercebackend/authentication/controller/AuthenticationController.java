package com.backend.ecommercebackend.authentication.controller;

import com.backend.ecommercebackend.authentication.dto.request.AuthRequest;
import com.backend.ecommercebackend.authentication.dto.request.LogoutRequest;
import com.backend.ecommercebackend.authentication.dto.request.RegisterRequest;
import com.backend.ecommercebackend.authentication.dto.response.AuthResponse;
import com.backend.ecommercebackend.authentication.service.CustomOauth2UserService;
import com.backend.ecommercebackend.authentication.service.impl.AuthenticationServiceImpl;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {
    private final AuthenticationServiceImpl service;


    @Autowired
    private CustomOauth2UserService customOauth2UserService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@ModelAttribute @Valid RegisterRequest request,@RequestParam("profileImg") MultipartFile imageFile){
        return ResponseEntity.ok(service.register(request,imageFile));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@Valid @RequestBody AuthRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshAuthToken(HttpServletRequest request) throws IOException {
        AuthResponse authResponse = service.refreshAuthToken(request);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequest request) {
        service.logout(request);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/google-login")
    public ResponseEntity<AuthResponse> googleLogin(@RequestBody Map<String, String> body) throws Exception {
        String idToken = body.get("id_token");
        try {
            AuthResponse response = customOauth2UserService.processGoogleLogin(idToken);
            return ResponseEntity.ok(response);
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
