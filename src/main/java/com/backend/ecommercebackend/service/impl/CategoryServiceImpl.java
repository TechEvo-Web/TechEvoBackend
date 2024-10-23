package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.ProductSpecificationDto;
import com.backend.ecommercebackend.dto.request.CategoryRequest;
import com.backend.ecommercebackend.dto.response.CategoryResponse;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.mapper.CategoryMapper;
import com.backend.ecommercebackend.model.product.Category;
import com.backend.ecommercebackend.repository.product.CategoryRepository;
import com.backend.ecommercebackend.repository.product.SpecificationRepository;
import com.backend.ecommercebackend.service.CategoryService;
import com.backend.ecommercebackend.service.ProductService;
import com.backend.ecommercebackend.service.SpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper categoryMapper;
    private final SpecificationRepository specificationRepository;
    private final SpecificationService specificationService;
    private final ProductService productService;


    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
       Category save = categoryMapper.CategoryDtoToEntity(categoryRequest);
        repository.save(save);
        return categoryMapper.EntityToCategoryDto(save);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryMapper.CategoryEntityToCategoryDtoList(repository.findAll());
    }

    @Override
    public CategoryResponse updateCategory(int categoryId, CategoryRequest categoryRequest) {
        Category category = repository.findById(categoryId).orElseThrow(()-> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
        List<ProductSpecificationDto>specifications=category.getSpecifications();
        Category save = categoryMapper.updateCategoryFromDto(categoryRequest,category);
        save.setSpecifications(specifications);
        repository.save(save);
        return categoryMapper.EntityToCategoryDto(save);
    }

    @Override
    public void deleteCategory(int categoryId) {
        specificationRepository.deleteByCategoryId(categoryId);
        repository.deleteById(categoryId);
    }

    @Override
    public Object getFiltersByCategoryName(String categoryName) {
        List<String> filterNames = specificationService.getFilterSpecificationsByCategoryName(categoryName);
        List<ProductResponse> products = productService.getProductsByCategoryName(categoryName);
        Map<String, Set<String>> filters = new HashMap<>();
        for (String filterName : filterNames) {
            filters.put(filterName, new HashSet<>());
        }
        for (ProductResponse product : products) {
            for (String filterName : filterNames) {
                String filterValue = product.getSpecifications().get(filterName);
                if (filterValue != null) {
                    filters.get(filterName).add(filterValue);
                }
            }
        }

        return filters;
    }


}
