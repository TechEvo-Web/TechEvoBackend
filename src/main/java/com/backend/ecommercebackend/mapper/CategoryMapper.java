package com.backend.ecommercebackend.mapper;
import com.backend.ecommercebackend.dto.request.CategoryRequest;
import com.backend.ecommercebackend.model.product.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category CategoryDtoToEntity(CategoryRequest categoryRequest);
    Category updateCategoryFromDto(CategoryRequest categoryRequest, @MappingTarget Category category);
}
