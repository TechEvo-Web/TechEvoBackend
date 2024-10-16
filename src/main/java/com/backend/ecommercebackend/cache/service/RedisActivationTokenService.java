package com.backend.ecommercebackend.cache.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisActivationTokenService {

  @Value("${activation-token.expiration}")
  private Long ttl;

  private final RedisTemplate<String,String> redisTemplate;
  private static final String ACTIVATION_TOKEN_KEY_PREFIX = "activation_token:";

  public void storeActivationToken(String email, String token) {
    String redisKey = ACTIVATION_TOKEN_KEY_PREFIX + email;
    try {
      redisTemplate.opsForValue().set(redisKey, token, ttl, TimeUnit.MILLISECONDS);
    }
    catch (Exception e) {
      log.error("Failed to store activation token in Redis", e);
    }
  }

  public String getActivationToken(String email) {
    String redisKey = ACTIVATION_TOKEN_KEY_PREFIX + email;
    var token = redisTemplate.opsForValue().get(redisKey);
    try {
      if (token == null) {
        log.error("Activation token not found for email: {}", email);
      }
    } catch (Exception e) {
      log.error("Failed to retrieve activation token in Redis", e);
    }
    return token;
  }

  public void deleteActivationToken(String email) {
    String redisKey = ACTIVATION_TOKEN_KEY_PREFIX + email;
    redisTemplate.delete(redisKey);
  }
}
