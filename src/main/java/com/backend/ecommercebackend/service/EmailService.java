package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.EmailActivationRequest;
import com.backend.ecommercebackend.dto.request.EmailRequest;

public interface EmailService {
    void sendVerificationCode(String email);
    void sendActivationLink(String email);
    void registerEmail(EmailRequest request);
    void activateEmail(EmailActivationRequest request);
    void deleteStoredEmail(String email);
}
