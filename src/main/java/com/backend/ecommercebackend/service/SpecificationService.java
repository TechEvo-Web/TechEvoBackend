package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.ProductSpecificationRequest;
import com.backend.ecommercebackend.dto.response.ProductSpecificationResponse;
import java.util.List;

public interface SpecificationService {
    List<ProductSpecificationResponse> getAllSpecifications();
    ProductSpecificationResponse addSpecification(ProductSpecificationRequest specificationRequest);
    ProductSpecificationResponse updateSpecification(Long specificationId, ProductSpecificationRequest specificationRequest);
    void deleteSpecification(Long id, int categoryId);
}
