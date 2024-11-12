package com.backend.ecommercebackend.mapper;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import com.backend.ecommercebackend.model.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  @Mapping(target = "specifications",ignore = true)
  Product ProductDtoToEntity(ProductRequest request);
  ProductResponse toProductResponse(Product product, boolean isFav);
  @Mapping(target = "specifications",ignore = true)
  Product updateProductFromProductDto(ProductRequest productRequest, @MappingTarget Product product);
}
