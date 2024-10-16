package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.ChangePasswordRequest;
import com.backend.ecommercebackend.dto.request.EmailVerifyRequest;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.model.user.User;
import com.backend.ecommercebackend.repository.user.UserRepository;
import com.backend.ecommercebackend.service.ChangePasswordService;
import com.backend.ecommercebackend.cache.service.RedisVerificationService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
@RequiredArgsConstructor
public class ChangePasswordServiceImpl implements ChangePasswordService {
    private final PasswordEncoder passwordEncoder;
    private final RedisVerificationService redisVerificationService;
    private final UserRepository repository;

    @Override
    public String changePassword(UserDetails userDetails, ChangePasswordRequest changePasswordRequest) {
        User user = repository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND));

        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new ApplicationException(Exceptions.PASSWORD_SAME_AS_OLD_EXCEPTION);
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new ApplicationException(Exceptions.PASSWORD_MISMATCH_EXCEPTION);
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        repository.save(user);

        return "Password changed successfully";
    }

    @Override
    public Boolean verifyEmail(@RequestBody EmailVerifyRequest emailVerifyRequest) {
       String verificationCode = redisVerificationService.getVerificationCode(emailVerifyRequest.getEmail());

        try {
            if (verificationCode == null) {
                System.out.println("VerificationCode not found");
                return false;
            }
            return emailVerifyRequest.getVerificationCode().equals(verificationCode);
        } catch (Exception e) {
            System.out.println("Error during verification: " + e.getMessage());
            return false;
        }
    }


}
