package com.backend.ecommercebackend.cache.service;


import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class VisitCounterService {

  private static final String TOTAL_VISIT_COUNT_KEY = "total-visit-count";
  private static final String WEEKLY_VISIT_COUNT_KEY_PREFIX = "visit-count-week-";

  private final RedisTemplate<String, String> redisTemplate;

  public void incrementVisitCount() {
    redisTemplate.opsForValue().increment(TOTAL_VISIT_COUNT_KEY);
    int currentWeek = getCurrentWeekOfMonth();
    String weeklyKey = WEEKLY_VISIT_COUNT_KEY_PREFIX + currentWeek;
    redisTemplate.opsForValue().increment(weeklyKey);
  }

  public Long getVisitCount() {
    String count = redisTemplate.opsForValue().get(TOTAL_VISIT_COUNT_KEY);
    return count != null ? Long.parseLong(count) : 0L;
  }

  public List<Long> getWeeklyVisitCounts(Integer week) {
    List<Long> weeklyCounts = new ArrayList<>();

    if (week != null) {
      if (week < 1 || week > 4) {
        throw new ApplicationException(Exceptions.INVALID_NUMBER_RANGE_EXCEPTION,"Week number must be between 1 and 4");
      }
      String weeklyKey = WEEKLY_VISIT_COUNT_KEY_PREFIX + week;
      String count = redisTemplate.opsForValue().get(weeklyKey);
      weeklyCounts.add(count != null ? Long.parseLong(count) : 0L);
    }
    else {
      for (int i = 1; i <= 4; i++) {
        String weeklyKey = WEEKLY_VISIT_COUNT_KEY_PREFIX + i;
        String count = redisTemplate.opsForValue().get(weeklyKey);
        weeklyCounts.add(count != null ? Long.parseLong(count) : 0L);
      }

      String week5Count = redisTemplate.opsForValue().get(WEEKLY_VISIT_COUNT_KEY_PREFIX + 5);
      if (week5Count != null) {
        Long week5 = Long.parseLong(week5Count);
        weeklyCounts.set(3, weeklyCounts.get(3) + week5);
        redisTemplate.delete(WEEKLY_VISIT_COUNT_KEY_PREFIX + 5);
      }
    }

    return weeklyCounts;
  }


  private int getCurrentWeekOfMonth() {
    LocalDate today = LocalDate.now();
    int dayOfMonth = today.getDayOfMonth();
    int week = (int) Math.ceil((double) dayOfMonth / 7);
    return Math.min(week, 4);
  }
}
