package com.backend.ecommercebackend.service;

import java.util.Map;

public interface AdminService {
    Map<String, Map<String, Object>> getAdminAnalytics();
    Map<String,Object>getAllStatistics();
}
