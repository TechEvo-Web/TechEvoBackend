package com.backend.ecommercebackend.mapper;
import com.backend.ecommercebackend.dto.request.ProductSpecificationRequest;
import com.backend.ecommercebackend.model.product.ProductSpecification;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecificationMapper {
    ProductSpecification SpecificationDtoToEntity(ProductSpecificationRequest productSpecificationRequest);
    ProductSpecification updateSpecificationFromDto(ProductSpecificationRequest specificationDto, @MappingTarget ProductSpecification specification);
}
