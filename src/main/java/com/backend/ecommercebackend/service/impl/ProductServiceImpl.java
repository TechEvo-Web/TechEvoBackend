package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.dto.response.ProductResponse;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.mapper.ProductMapper;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.repository.product.CommentRepository;
import com.backend.ecommercebackend.repository.product.ProductRepository;
import com.backend.ecommercebackend.service.FileStorageService;
import com.backend.ecommercebackend.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductMapper mapper;
    private final ProductRepository repository;
    private final FileStorageService fileStorageService;
    private final CommentRepository commentRepository;
    private final SpecificationServiceImpl specificationServiceImpl;

    @Override
    public ProductResponse addProduct(ProductRequest request, List<MultipartFile> imageFiles) {
        Product product = mapper.ProductDtoToEntity(request);
        Map<String, Object> specifications = stringParseJson(request.getSpecifications());
        product.setSpecifications(specifications);

        List<String> imageUrls = new ArrayList<>();
        addImage(imageFiles, product, imageUrls);
        repository.save(product);
        return mapper.EntityToProductDto(product);
    }

    private Map<String, Object> stringParseJson(String requestSpecifications) {
        Map<String, Object> specifications = new HashMap<>();
        if (requestSpecifications != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                specifications = objectMapper.readValue(requestSpecifications, new TypeReference<Map<String, Object>>() {
                });
            } catch (JsonProcessingException e) {
                throw new ApplicationException(Exceptions.INVALID_FORMAT_EXCEPTION);
            }
        }
        return specifications;
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request, List<MultipartFile> imageFiles) throws IOException {
        Product product = repository.findById(id).orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
        mapper.updateProductFromProductDto(request, product);
        List<String> imageUrls = new ArrayList<>();
        Map<String, Object> specifications = stringParseJson(request.getSpecifications());
        product.setSpecifications(specifications);
        for (String imageUrl : product.getImageUrl()) {
            if (imageUrl != null) {
                fileStorageService.deleteFile(imageUrl);
            }
        }
        product.setImageUrl(imageUrls);
        addImage(imageFiles, product, imageUrls);
        repository.save(product);
        return mapper.EntityToProductDto(product);
    }

    public void addImage(List<MultipartFile> imageFiles, Product product, List<String> imageUrls) {
        for (MultipartFile imageFile : imageFiles) {
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    String url = fileStorageService.storeImages(imageFile, "productImages");
                    imageUrls.add(url);
                    product.setImageUrl(imageUrls);

                } catch (IOException e) {
                    throw new ApplicationException(Exceptions.IMAGE_STORAGE_EXCEPTION);
                }
            }
        }
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = repository.findById(id).orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
        return mapper.EntityToProductDto(product);
    }

    @Override
    public List<ProductResponse> getProductsByCategoryName(String categoryName) {
        List<Product> products = repository.findByCategoryName(categoryName);
        return mapper.EntityListToProductDtoList(products);
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        return mapper.EntityListToProductDtoList(repository.findAll());
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = repository.findById(id).orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
        for (String imageUrl : product.getImageUrl()) {
            if (imageUrl != null) {
                try {
                    fileStorageService.deleteFile(imageUrl);
                } catch (IOException e) {
                    log.error("Failed to delete image");
                }
            }
        }
        commentRepository.deleteByProductId(id);
        repository.deleteById(id);
    }

    @Override
    public List<Product> getFilteringProducts(Float min, Float max, String categoryName, Map<String, String> filterSpec) {
        List<Product> result = new ArrayList<>();
        List<Product> allProducts = repository.findAll();
        if (min != null && max != null) {
            result = allProducts.stream().filter(item -> item.getPrice() >= min && item.getPrice() < max).toList();
        }
        if (Objects.nonNull(filterSpec)) {
            List<Product> filter = new ArrayList<>();
            for (Product product : result) {
                List<String> filterSpecNames = specificationServiceImpl.getFilterSpecificationsByCategoryName(categoryName);
                for (String specNames : filterSpecNames) {
                    String value = product.getSpecifications().get(specNames).toString();
                    if (value != null) {
                        product.getSpecifications().entrySet().stream().map(item->{
                            if(item.getKey().equals(specNames)) {
                                filter.add(product);
                            }
                            return item;
                        });
                    }
                }
            }
            result = filter;
        }
        return result;
    }
}
