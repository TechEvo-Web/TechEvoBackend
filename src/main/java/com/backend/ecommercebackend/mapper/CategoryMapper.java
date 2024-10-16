package com.backend.ecommercebackend.mapper;
import com.backend.ecommercebackend.dto.request.CategoryRequest;
import com.backend.ecommercebackend.dto.response.CategoryResponse;
import com.backend.ecommercebackend.model.product.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category CategoryDtoToEntity(CategoryRequest categoryRequest);
    CategoryResponse EntityToCategoryDto(Category category);
    List<CategoryResponse> CategoryEntityToCategoryDtoList(List<Category> categoryList);
    Category updateCategoryFromDto(CategoryRequest categoryRequest, @MappingTarget Category category);

}
