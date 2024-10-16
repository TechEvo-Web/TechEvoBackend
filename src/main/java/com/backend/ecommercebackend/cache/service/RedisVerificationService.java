package com.backend.ecommercebackend.cache.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisVerificationService {
    private final RedisTemplate<String,String> redisTemplate;
    private static final String VERIFICATION_CODE_KEY_PREFIX = "verification_code:";
    @Value("${verification-code.expiration}")
    private int expirationForVerificationCode;
    public void storeEmailVerificationCode(String email, String code) {
        String redisKey = VERIFICATION_CODE_KEY_PREFIX + email;
        deleteVerificationCode(email);
        redisTemplate.opsForValue().set(redisKey,code,expirationForVerificationCode, TimeUnit.MILLISECONDS);
    }
    public String getVerificationCode(String email) {
        String redisKey = VERIFICATION_CODE_KEY_PREFIX + email;
        return redisTemplate.opsForValue().get(redisKey);

    }

    public void deleteVerificationCode(String email) {
        String redisKey = VERIFICATION_CODE_KEY_PREFIX + email;
        redisTemplate.delete(redisKey);
    }
}
