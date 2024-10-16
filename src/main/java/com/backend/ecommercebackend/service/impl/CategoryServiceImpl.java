package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.ProductSpecificationDto;
import com.backend.ecommercebackend.dto.request.CategoryRequest;
import com.backend.ecommercebackend.dto.response.CategoryResponse;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.mapper.CategoryMapper;
import com.backend.ecommercebackend.model.product.Category;
import com.backend.ecommercebackend.repository.product.CategoryRepository;
import com.backend.ecommercebackend.repository.product.SpecificationRepository;
import com.backend.ecommercebackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper categoryMapper;
    private final SpecificationRepository specificationRepository;

    @Override
    public List<String> getSpecsByCategoryId(int id) {
        return repository.findSpecificationNameByCategoryId(id);
    }

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



}
