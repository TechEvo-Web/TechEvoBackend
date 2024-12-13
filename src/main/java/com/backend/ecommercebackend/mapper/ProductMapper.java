package com.backend.ecommercebackend.mapper;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import com.backend.ecommercebackend.model.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  @Mapping(target = "isStock",ignore = true)
  Product ProductDtoToEntity(ProductRequest request);
  ProductResponse toProductResponse(Product product);
  Product updateProductFromProductDto(ProductRequest productRequest, @MappingTarget Product product);
}
