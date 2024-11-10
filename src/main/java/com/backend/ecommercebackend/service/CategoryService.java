package com.backend.ecommercebackend.service;


import com.backend.ecommercebackend.dto.request.CategoryRequest;
import com.backend.ecommercebackend.model.product.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryRequest categoryRequest);
    List<Category> getAllCategories();
    void deleteCategory(int categoryId);
    Category updateCategory(int categoryId, CategoryRequest categoryRequest);
    Object getFiltersByCategoryName(String categoryName);
}
