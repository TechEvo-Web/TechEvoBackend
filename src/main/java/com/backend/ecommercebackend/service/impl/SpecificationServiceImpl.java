package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.ProductSpecificationDto;
import com.backend.ecommercebackend.dto.request.ProductSpecificationRequest;
import com.backend.ecommercebackend.dto.response.ProductSpecificationResponse;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.mapper.SpecificationMapper;
import com.backend.ecommercebackend.model.product.Category;
import com.backend.ecommercebackend.model.product.ProductSpecification;
import com.backend.ecommercebackend.repository.product.CategoryRepository;
import com.backend.ecommercebackend.repository.product.SpecificationRepository;
import com.backend.ecommercebackend.service.SpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecificationServiceImpl implements SpecificationService {
    private final SpecificationRepository repository;
    private final SpecificationMapper mapper;
    private final CategoryRepository categoryRepository;

    @Override
    public List<ProductSpecificationResponse> getAllSpecifications() {
        return mapper.SpecificationEntityToSpecificationDtoList(repository.findAll());
    }

    @Override
    public ProductSpecificationResponse addSpecification(ProductSpecificationRequest specificationRequest) {
        ProductSpecification specification = mapper.SpecificationDtoToEntity(specificationRequest);
        repository.save(specification);
        Category category = categoryRepository.findById(specificationRequest.getCategoryId()).orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
        ProductSpecificationDto specificationDto = new ProductSpecificationDto();
        specificationDto.setSpecificationId(specification.getSpecificationId());
        specificationDto.setSpecificationName(specification.getSpecificationName());
        List<ProductSpecificationDto> specifications = category.getSpecifications();
        if(specifications==null){
            specifications = new ArrayList<>();
        }
        specifications.add(specificationDto);
        category.setSpecifications(specifications);
        categoryRepository.save(category);
        return mapper.EntityToSpecificationDto(specification);
    }

    @Override
    public ProductSpecificationResponse updateSpecification(Long specificationId, ProductSpecificationRequest specificationRequest) {
        ProductSpecification specification = repository.findById(specificationId).orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
        ProductSpecification save = mapper.updateSpecificationFromDto(specificationRequest, specification);
        Category category = categoryRepository.findById(specificationRequest.getCategoryId()).orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
        List<ProductSpecificationDto>specifications = category.getSpecifications();
        if(specifications==null){
            specifications = new ArrayList<>();
        }
        specifications.forEach(p -> {
            if(p.getSpecificationId().equals(specification.getSpecificationId())) {
                p.setSpecificationName(save.getSpecificationName());
            }
        });
        categoryRepository.save(category);
        repository.save(save);
        return mapper.EntityToSpecificationDto(save);
    }

    @Override
    public void deleteSpecification(Long id, int categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
        category.getSpecifications().removeIf(p -> p.getSpecificationId().equals(id));
        categoryRepository.save(category);
        repository.deleteById(id);
    }
}
