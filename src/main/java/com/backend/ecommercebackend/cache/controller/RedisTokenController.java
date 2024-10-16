package com.backend.ecommercebackend.cache.controller;

import com.backend.ecommercebackend.cache.service.RedisActivationTokenService;
import com.backend.ecommercebackend.cache.service.RedisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class RedisTokenController {

  private  final RedisTokenService redisTokenService;
  private final RedisActivationTokenService redisActivationTokenService;

  @GetMapping("/get")
  public ResponseEntity<String> getRefreshToken(@RequestParam("email") String email) {
    String refreshToken = redisTokenService.getRefreshToken(email);
    return ResponseEntity.ok(refreshToken);
  }

  @GetMapping("/getActivationToken")
  public ResponseEntity<String> getActivationToken(@RequestParam("email") String email) {
    return ResponseEntity.ok(redisActivationTokenService.getActivationToken(email));
  }

  @PostMapping("/store")
  public ResponseEntity<String> storeToken(@RequestParam("email") String email,
                                           @RequestParam("token") String token,
                                           @RequestParam("expirationTime") long expirationTime) {
    redisTokenService.storeRefreshToken(email, token, expirationTime);
    return ResponseEntity.ok("Token stored successfully");
  }

  @PostMapping("/storeActivationToken")
  public ResponseEntity<String> storeActivationToken(@RequestParam("email") String email,
                                                     @RequestParam("token") String token) {
    redisActivationTokenService.storeActivationToken(email, token);
    return ResponseEntity.ok("Token stored successfully");
  }

}