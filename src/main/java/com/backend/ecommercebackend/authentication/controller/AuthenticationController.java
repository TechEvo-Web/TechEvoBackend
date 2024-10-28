package com.backend.ecommercebackend.authentication.controller;

import com.backend.ecommercebackend.authentication.dto.request.AuthRequest;
import com.backend.ecommercebackend.authentication.dto.request.LogoutRequest;
import com.backend.ecommercebackend.authentication.dto.request.RegisterRequest;
import com.backend.ecommercebackend.authentication.dto.response.AuthResponse;
import com.backend.ecommercebackend.authentication.service.CustomOauth2UserService;
import com.backend.ecommercebackend.authentication.service.impl.AuthenticationServiceImpl;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Register istifade olunan endpoint.Data normal json data olaraq gonderilecek.")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Registerden sonra login üçün endpoint")
    public ResponseEntity<AuthResponse> login (@Valid @RequestBody AuthRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token ile access token almaq üçün endpoint",description = "Access token muddeti 10 deqiqe,refresh token muddeti ise 1 aydi.Ona gore access token muddeti bitdikde yenisini almaq üçün refresh tokeni authorization hissesinde gondermek lazimdi.")
    public ResponseEntity<AuthResponse> refreshAuthToken(HttpServletRequest request) throws IOException {
        AuthResponse authResponse = service.refreshAuthToken(request);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    @Operation(summary = "İstifadəçinin hesab çıxışı üçün endpoint")
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequest request) {
        service.logout(request);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/google-login")
    @Operation(summary = "Google linki istifadə edərək asan login üçün endpoint")
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
