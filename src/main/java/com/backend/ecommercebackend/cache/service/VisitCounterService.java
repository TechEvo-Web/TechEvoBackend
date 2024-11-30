package com.backend.ecommercebackend.cache.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class VisitCounterService {

  private static final String VISIT_COUNT_KEY = "total-visit-count";

  private final RedisTemplate<String, String> redisTemplate;

  public void incrementVisitCount() {
    try {
      redisTemplate.opsForValue().increment(VISIT_COUNT_KEY);
    } catch (Exception e) {
      log.error("Failed to increment visit count in Redis", e);
    }
  }

  public Long getVisitCount() {
    try {
      String count = redisTemplate.opsForValue().get(VISIT_COUNT_KEY);
      return count != null ? Long.parseLong(count) : 0L;
    } catch (Exception e) {
      log.error("Failed to fetch visit count from Redis", e);
      return 0L;
    }
  }
}
