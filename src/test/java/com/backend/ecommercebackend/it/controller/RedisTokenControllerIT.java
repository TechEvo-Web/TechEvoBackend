package com.backend.ecommercebackend.it.controller;

import com.backend.ecommercebackend.cache.service.RedisTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class RedisTokenControllerIT {

  private static final String REFRESH_TOKEN = "eyJhbGciOiJIUzUxMiJ9" +
          ".eyJzdWIiOiJrYXRlQGdtYWlsLmNvbSIsImlhdCI6MTcyNjM5MzgzNSwiZXhwIjoxNzI4OTg1ODM1fQ.vkt81z0O3msrIOs6GrjtlKgZSTXC-d1CeeCFzc4lJeqd25ipFDxnN2AfKGv6mQgnSJrncgqspHwFKv38NtgWMg";
  @Autowired
  private RedisTemplate<String, String> redisTemplate;
  @Autowired
  RedisTokenService redisTokenService;
  @Autowired
  private MockMvc mockMvc;


  @BeforeEach
  void resetRedis() {
    redisTemplate.keys("*refresh_token:kate@gmail.com*").forEach(redisTemplate::delete);
  }

  @Test
  @DisplayName("Store and retrieve refresh token")
  void shouldStoreAndRetrieveRefreshToken() throws Exception {

    mockMvc.perform(post("/api/v1/auth/store")
                    .param("email","kate@gmail.com")
                    .param("token",REFRESH_TOKEN)
                    .param("expirationTime", String.valueOf(2592000000L)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("Token stored successfully"));

    var storedToken = redisTokenService.getRefreshToken("kate@gmail.com");
    var refreshTokenCache = redisTemplate.opsForValue().get("refresh_token:kate@gmail.com");

    assertThat(refreshTokenCache).isNotNull();
    assertThat(storedToken).isEqualTo(REFRESH_TOKEN);
  }
}