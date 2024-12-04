package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.cache.service.VisitCounterService;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.model.order.Order;
import com.backend.ecommercebackend.model.order.OrderStatus;
import com.backend.ecommercebackend.model.user.User;
import com.backend.ecommercebackend.repository.order.OrderRepository;
import com.backend.ecommercebackend.repository.user.UserRepository;
import com.backend.ecommercebackend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final VisitCounterService visitCounterService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    public Map<String, Map<String, Object>> getAdminAnalytics() {
        Map<String, Map<String, Object>> analytics = new HashMap<>();
        List<User> users = userRepository.findAll();
        List<Order> orders = orderRepository.findAll();

        LocalDateTime now = LocalDateTime.now();
        for (int i = 1; i <= 4; i++) {
            long userCount = 0;
            long orderCount = 0;
            int weekDays = 7;
//            System.out.println(day + " day deyeri");
            Map<String, Object> analyticValue = new HashMap<>();
            for (int day = weekDays * (i - 1) + 1; day <= weekDays * i; day++) {
                for (User user : users) {
                    LocalDateTime userCreatedTime = user.getCreatedAt();
                    if (userCreatedTime.getDayOfMonth() == day && userCreatedTime.getMonthValue() == now.getMonthValue() && userCreatedTime.getYear() == now.getYear()) {
                        userCount++;
                    }
                }
                for (Order order : orders) {
                    LocalDateTime orderCreatedTime = order.getCreatedAt();
                    if (orderCreatedTime.getDayOfMonth() == day && orderCreatedTime.getMonthValue() == now.getMonthValue() && orderCreatedTime.getYear() == now.getYear()) {
                        orderCount++;
                    }
                }
            }
            analyticValue.put("loginUserCount", userCount);
            analyticValue.put("orderCount", orderCount);
            analyticValue.put("visitCount", visitCounterService.getWeeklyVisitCounts(i).get(0));
            analytics.put("week" + i, analyticValue);
            System.out.println("week"+i + " userCount deyeri" + userCount);

        }


        return analytics;
    }

    @Override
    public Map<String, Object> getAllStatistics() {
        Map<String, Object> result = new HashMap<>();
        Long loginUserCount = (long) userRepository.findAll().size();
        Long expectingOrderCount = (long) orderRepository.findOrdersByOrderStatus(OrderStatus.Pending).size();
        Long successOrderCount = (long) orderRepository.findOrdersByOrderStatus(OrderStatus.Delivered).size();
        Long visitCount = visitCounterService.getVisitCount();

        result.put("expectingOrderCount", expectingOrderCount);
        result.put("successOrderCount", successOrderCount);
        result.put("loginUserCount", loginUserCount);
        result.put("visitCount", visitCount);
        return result;
    }

}
