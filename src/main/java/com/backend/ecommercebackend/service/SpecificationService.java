package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.ProductSpecificationRequest;
import com.backend.ecommercebackend.model.product.ProductSpecification;

import java.util.List;

public interface SpecificationService {
    List<ProductSpecification> getAllSpecifications();
    ProductSpecification addSpecification(ProductSpecificationRequest specificationRequest);
    ProductSpecification updateSpecification(Long specificationId, ProductSpecificationRequest specificationRequest);
    void deleteSpecification(Long id, int categoryId);
    List<String> getFilterSpecificationsByCategoryName(String categoryName);
    List<String> getSpecsByCategoryId(int categoryId);
}
