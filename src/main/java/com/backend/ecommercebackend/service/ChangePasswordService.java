package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.ChangePasswordRequest;
import com.backend.ecommercebackend.dto.request.EmailVerifyRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface ChangePasswordService {
    String changePassword(UserDetails userDetails, ChangePasswordRequest changePasswordRequest);

    Boolean verifyEmail(EmailVerifyRequest emailVerifyRequest);
}
