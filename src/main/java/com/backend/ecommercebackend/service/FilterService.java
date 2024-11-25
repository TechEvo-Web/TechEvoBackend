package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.model.product.Product;
import java.util.List;
import java.util.Map;

public interface FilterService {
  List<Product> getFilteringProducts(Float min, Float max, Map<String, String> filterSpec, String categoryName);
  Map<String, Map<String, Object>> createPcFilter(Map<String, String> filter);
}
