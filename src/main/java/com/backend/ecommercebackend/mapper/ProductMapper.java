package com.backend.ecommercebackend.mapper;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import com.backend.ecommercebackend.model.product.Product;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  List<ProductResponse> EntityListToProductDtoList(List<Product> products);
  ProductResponse EntityToProductDto(Product product);
  Product ProductDtoToEntity(ProductRequest productRequest);
  Product updateProductFromProductDto(ProductRequest productRequest, @MappingTarget Product product);
}
