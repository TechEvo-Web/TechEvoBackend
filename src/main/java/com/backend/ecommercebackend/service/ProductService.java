package com.backend.ecommercebackend.service;
import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import com.backend.ecommercebackend.dto.request.RecommendProductRequest;
import com.backend.ecommercebackend.model.product.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductService {
     Product addProduct(ProductRequest request, List<MultipartFile> imageFile);
     Product getProductById(Long id);
     List<Product> getProductsByCategoryName(String categoryName);
     List<ProductResponse> getAllProduct(String token);
     Product updateProduct(Long id, ProductRequest request, List<MultipartFile> imageFile) throws IOException;
     void deleteProduct(Long id);
     List<Product> getFilteringProducts(Float min, Float max, Map<String, String> filterSpec);
     List<Product>  findRecommendedProduct(RecommendProductRequest request);
}
