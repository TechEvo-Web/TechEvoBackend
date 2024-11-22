package com.backend.ecommercebackend.service;

import com.backend.ecommercebackend.dto.request.SelectProductRequest;
import com.backend.ecommercebackend.model.product.Product;
import java.util.List;

public interface SelectiveProductService {
  List<Product> findSelectiveProduct(SelectProductRequest request);
}
