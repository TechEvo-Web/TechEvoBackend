package com.backend.ecommercebackend.service;


import com.backend.ecommercebackend.dto.request.CategoryRequest;
import com.backend.ecommercebackend.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<String> getSpecsByCategoryId(int categoryId);
    CategoryResponse createCategory(CategoryRequest categoryRequest);
    List<CategoryResponse> getAllCategories();
    void deleteCategory(int categoryId);
    CategoryResponse updateCategory(int categoryId, CategoryRequest categoryRequest);
}
