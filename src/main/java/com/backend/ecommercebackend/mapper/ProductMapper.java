package com.backend.ecommercebackend.mapper;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import com.backend.ecommercebackend.model.product.Product;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  List<ProductResponse> EntityListToProductDtoList(List<Product> products);
  ProductResponse EntityToProductDto(Product product);
  @Mapping(target = "specifications",ignore = true)
  Product ProductDtoToEntity(ProductRequest request);
  @Mapping(target = "specifications",ignore = true)
  Product updateProductFromProductDto(ProductRequest productRequest, @MappingTarget Product product);
}
