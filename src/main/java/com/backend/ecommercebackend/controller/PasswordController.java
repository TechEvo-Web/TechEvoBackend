package com.backend.ecommercebackend.controller;
import com.backend.ecommercebackend.dto.request.ChangePasswordRequest;
import com.backend.ecommercebackend.dto.request.EmailVerifyRequest;
import com.backend.ecommercebackend.service.impl.ChangePasswordServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class PasswordController {
    private final ChangePasswordServiceImpl service;

    @PostMapping("/changePassword")
    @Operation(summary = "İstifadəçi şifrəsini dəyişmək üçün endpoint",
            description = "Burda elave olaraq accestoken gonderilmelidi.Bu endpointden evvel sendVerificationCoke endpointi istifade olunacaq.Email gelen dogrulama kodu ve email verify endpointine gonderilecek.Eger cavab true dönsə o zaman changePassword hissesi açılacaq.Və bu endpoint sonra istifadə olunacaq.")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        String message = service.changePassword(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    @Operation(summary = "Təsdiqləmə üçün endpoint",
            description = "Bu endpointe sendVerificationCode istifade olunduqdan sonra gelen verification code ve hemin email gonderilecek.Cavab true olsa changePassword sehifesine kecide icaze olacaq.")
    public ResponseEntity<Boolean> verifyEmail(@Valid @RequestBody EmailVerifyRequest request){
        return ResponseEntity.ok(service.verifyEmail(request));
    }
}
