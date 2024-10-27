package com.backend.ecommercebackend.controller;

import com.backend.ecommercebackend.dto.request.EmailActivationRequest;
import com.backend.ecommercebackend.dto.request.EmailRequest;
import com.backend.ecommercebackend.service.impl.EmailServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
@Validated
public class EmailController {
    private final EmailServiceImpl emailService;

    @PostMapping("/sendVerificationCode")
    @Operation(summary = "Şifrə dəyişəndə emailə mesaj göndərmək üçün endpoint", description = "param ilə email göndərmək lazımdır.")
    public void sendEmail(@RequestParam("email") String email) {
        emailService.sendVerificationCode(email);
    }

    @PostMapping("/register-email")
    @Operation(summary = "İlk register səhifəsi üçün endpoint",
            description = "İlk öncə bu endpointə email gonderilecek.Daha sonra activate endpointinə bu email və emailə gələn urldən alınan token göndərildikdən sonra bu email register oluna biləcək.")
    public ResponseEntity<String> storeEmail(@Valid @RequestBody EmailRequest request) {
        emailService.registerEmail(request);
        return ResponseEntity.ok("User email stored successfully with unverified");
    }

    @PostMapping("/activate")
    @Operation(summary = "Register olunan emaili aktiv etmək üçün endpoint")
    public ResponseEntity<String> activateUser(@Valid @RequestBody EmailActivationRequest request) {
        emailService.activateEmail(request);
        return ResponseEntity.ok("User email activated successfully");
    }
}
