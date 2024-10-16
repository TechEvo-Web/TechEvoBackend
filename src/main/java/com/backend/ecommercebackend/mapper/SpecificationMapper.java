package com.backend.ecommercebackend.mapper;
import com.backend.ecommercebackend.dto.request.ProductSpecificationRequest;
import com.backend.ecommercebackend.dto.response.ProductSpecificationResponse;
import com.backend.ecommercebackend.model.product.ProductSpecification;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecificationMapper {
    List<ProductSpecificationResponse> SpecificationEntityToSpecificationDtoList(List<ProductSpecification> specificationList);
    ProductSpecification SpecificationDtoToEntity(ProductSpecificationRequest productSpecificationRequest);
    ProductSpecificationResponse EntityToSpecificationDto(ProductSpecification specification);
    ProductSpecification updateSpecificationFromDto(ProductSpecificationRequest specificationDto, @MappingTarget ProductSpecification specification);
}
