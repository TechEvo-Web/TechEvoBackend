package com.backend.ecommercebackend.controller;
import com.backend.ecommercebackend.dto.request.ChangePasswordRequest;
import com.backend.ecommercebackend.dto.request.EmailVerifyRequest;
import com.backend.ecommercebackend.service.impl.ChangePasswordServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class PasswordController {
    private final ChangePasswordServiceImpl service;

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                                 @Valid @RequestBody ChangePasswordRequest request ){
        return ResponseEntity.ok(service.changePassword(userDetails,request));
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyEmail(@Valid @RequestBody EmailVerifyRequest request){
        return ResponseEntity.ok(service.verifyEmail(request));
    }
}
