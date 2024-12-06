package com.backend.ecommercebackend.service.impl;


import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.repository.product.ProductRepository;
import com.backend.ecommercebackend.repository.product.SpecificationRepository;
import com.backend.ecommercebackend.service.FilterService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {

    private final ProductRepository repository;
    private final SpecificationRepository specificationRepository;

    @Override
    public List<Product> getFilteringProducts(Float min, Float max, Map<String, String> filterSpec, String categoryName) {
        List<Product> result = new ArrayList<>();
        List<Product> allProducts;
        List<Product> filter = new ArrayList<>();
        List<String> objectTypeSpecifications = new ArrayList<>();
        if (categoryName != null) {
            allProducts = repository.findByCategoryName(categoryName);
        } else {
            allProducts = repository.findAll();
        }
        if (min != null && max != null) {
            result = allProducts.stream().filter(item -> item.getPrice() >= min && item.getPrice() < max).toList();
        } else {
            result = allProducts;
        }

        if (!filterSpec.isEmpty()) {
            boolean matches = true;
            for (Product product : result) {
                matches = true;
                for (Map.Entry<String, String> entry : filterSpec.entrySet()) {
                    String filterValue = entry.getValue().replace("\"", "").trim();
                    String filterKey = entry.getKey();
                    String nestedKey = "";
                    String[] keyValues;
                    if (filterKey.contains(".")) {
                        keyValues = filterKey.split("\\.");
                        filterKey = keyValues[0];
                        nestedKey = keyValues[keyValues.length - 1];
                        System.out.println(filterKey + " " + nestedKey);
                    }
                    objectTypeSpecifications = specificationRepository.findBySpecificationName(filterKey);
                    System.out.println(objectTypeSpecifications);
                    if (objectTypeSpecifications.contains("Object")) {
                        if (product.getSpecifications().containsKey(filterKey)) {
                            Object productObjectValue = product.getSpecifications().get(filterKey);
                            System.out.println(productObjectValue + " " + "productObjectValue");
                            Object productValue;
                            if (productObjectValue instanceof Map) {
                                Map<String, Object> productValueMap = (Map<String, Object>) productObjectValue;
                                productValue = productValueMap.get(nestedKey);
                                boolean filterBoolean = Boolean.parseBoolean(filterValue);
                                System.out.println("productValue " + productValue);
                                System.out.println("filterValue " + filterValue + " " + ((Object) filterValue).getClass().getName());
                                if (!Objects.equals(productValue, filterBoolean)) {
                                    matches = false;
                                    System.out.println("matches " + matches);
                                }
                            }
                        }
                    } else {
                        if (product.getSpecifications().containsKey(filterKey)) {
                            Object productValue = product.getSpecifications().get(filterKey);
                            if (!Objects.equals(productValue, filterValue)) {
                                matches = false;
                            }
                        }
                    }
                }
                if (matches) {
                    filter.add(product);
                }
            }

            result = filter;
            if (result.isEmpty()) {
                return new ArrayList<>();
            }
        }
        return result;
    }

    @Override
    public Map<String, Map<String, Object>> createPcFilter(Map<String, String> filter) {
        List<Product> products = repository.findAll();
        Map<String, Map<String, Object>> defaultParts = new HashMap<>();
        List<String> categories = List.of("Ram", "Cpu", "Psu", "Gpu", "Ssd", "Hdd", "Case", "Motherboard");

        for (Product product : products) {
            if (categories.contains(product.getCategoryName())) {
                defaultParts.computeIfAbsent(product.getCategoryName(), k -> new HashMap<>()).put(product.getName(), false);
            }
        }
        for (Product product : products) {
            if (categories.contains(product.getCategoryName())) {
                boolean isCompatible = true;
                for (Map.Entry<String, String> entry : filter.entrySet()) {
                    String filterKey = entry.getKey();
                    String filterValue = entry.getValue();
                    Map<String, Object> compatibleObject = (Map<String, Object>) product.getSpecifications().get("compatible");
                    if (compatibleObject.containsKey(filterKey)) {
                        List<String> compatibleValues = (List<String>) compatibleObject.get(filterKey);
                        if (!compatibleValues.contains(filterValue)) {
                            isCompatible = false;
                            break;
                        }
                    }
                }
                if (isCompatible) {
                    defaultParts.get(product.getCategoryName()).put(product.getName(), true);
                }
            }
        }

        return defaultParts;
    }
}
