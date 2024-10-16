package com.backend.ecommercebackend.service;
import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
     ProductResponse addProduct(ProductRequest request, List<MultipartFile> imageFile);
     ProductResponse getProductById(Long id);
     List<ProductResponse> getProductsByCategoryName(String categoryName);
     List<ProductResponse> getAllProduct();
     ProductResponse updateProduct(Long id, ProductRequest request, List<MultipartFile> imageFile) throws IOException;
     void deleteProduct(Long id);
     ProductResponse rateProduct(Long id, float newRating);

}
