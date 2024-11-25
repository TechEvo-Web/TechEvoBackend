package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.SelectProductRequest;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.repository.product.ProductRepository;
import com.backend.ecommercebackend.service.SelectiveProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SelectiveProductServiceImpl implements SelectiveProductService {

  private final ProductRepository productRepository;

  @Override
  public List<Product> findSelectiveProduct(SelectProductRequest request) {
    String usingPurpose = request.getUsingPurpose();
    String whereUse = request.getWhereUse();
    String look = request.getLook();
    return productRepository.findAll().stream()
            .filter(product -> usingPurpose != null && usingPurpose.trim().equalsIgnoreCase(product.getUsingPurpose().trim()))
            .filter(product -> whereUse != null && whereUse.trim().equalsIgnoreCase(product.getWhereUse().trim()))
            .filter(product -> look != null && look.trim().equalsIgnoreCase(product.getLook().trim()))
            .toList();
  }
}
