package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.ProductSpecificationDto;
import com.backend.ecommercebackend.dto.request.CategoryRequest;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.mapper.CategoryMapper;
import com.backend.ecommercebackend.model.product.Category;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.repository.product.CategoryRepository;
import com.backend.ecommercebackend.repository.product.SpecificationRepository;
import com.backend.ecommercebackend.service.CategoryService;
import com.backend.ecommercebackend.service.ProductService;
import com.backend.ecommercebackend.service.SpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper categoryMapper;
    private final SpecificationRepository specificationRepository;
    private final SpecificationService specificationService;
    private final ProductService productService;


    @Override
    public Category createCategory(CategoryRequest categoryRequest) {
        Category save = categoryMapper.CategoryDtoToEntity(categoryRequest);
        return repository.save(save);

    }

    @Override
    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    @Override
    public Category updateCategory(int categoryId, CategoryRequest categoryRequest) {
        Category category = repository.findById(categoryId).orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
        List<ProductSpecificationDto> specifications = category.getSpecifications();
        Category save = categoryMapper.updateCategoryFromDto(categoryRequest, category);
        save.setSpecifications(specifications);
        return repository.save(save);

    }

    @Override
    public void deleteCategory(int categoryId) {
        specificationRepository.deleteByCategoryId(categoryId);
        repository.deleteById(categoryId);
    }

    @Override
    public Object getFiltersByCategoryName(String categoryName) {
        List<String> filterNames = specificationService.getFilterSpecificationsByCategoryName(categoryName);
        List<Product> products = productService.getProductsByCategoryName(categoryName);
        List<String> objectTypeSpecifications = new ArrayList<>();
        Map<String, Set<Object>> filters = new HashMap<>();

        for (String filterName : filterNames) {
            filters.put(filterName, new HashSet<>());
        }
        for (Product product : products) {
            for (String filterName : filterNames) {
                objectTypeSpecifications = specificationRepository.findBySpecificationName(filterName);
                if (objectTypeSpecifications.contains("Object")) {
                    Object filterObjectValue = product.getSpecifications().get(filterName);
                    if (filterObjectValue instanceof Map) {
                        Map<String, Object> filterValue = (Map<String,Object>) filterObjectValue;
                        for (String value : filterValue.keySet())
                            filters.get(filterName).add(value);
                    }
                } else {
                    filters.put(filterName, new HashSet<>());
                    Object filterValue = product.getSpecifications().get(filterName);
                    if (filterValue != null) {
                        filters.get(filterName).add(filterValue);
                    }
                }
            }
        }

        return filters;
    }


}
