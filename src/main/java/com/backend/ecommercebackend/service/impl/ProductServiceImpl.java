package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.dto.request.RecommendProductRequest;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.mapper.ProductMapper;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.repository.product.CommentRepository;
import com.backend.ecommercebackend.repository.product.ProductRepository;
import com.backend.ecommercebackend.repository.product.SpecificationRepository;
import com.backend.ecommercebackend.service.FileStorageService;
import com.backend.ecommercebackend.service.ProductService;
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
    private final SpecificationRepository specificationRepository;

    @Override
    public Product addProduct(ProductRequest request, List<MultipartFile> imageFiles) {
        Product product = mapper.ProductDtoToEntity(request);
        List<String> imageUrls = new ArrayList<>();
        addImage(imageFiles, product, imageUrls);
        return repository.save(product);
    }

    @Override
    public Product updateProduct(Long id, ProductRequest request, List<MultipartFile> imageFiles) throws IOException {
        Product product = repository.findById(id).orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
        mapper.updateProductFromProductDto(request, product);
        List<String> imageUrls = new ArrayList<>();
        if (imageFiles != null) {
            for (String imageUrl : product.getImageUrl()) {
                fileStorageService.deleteFile(imageUrl);
            }
            product.setImageUrl(imageUrls);
            addImage(imageFiles, product, imageUrls);
        }
        return repository.save(product);
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
    public Product getProductById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION));
    }

    @Override
    public List<Product> getProductsByCategoryName(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION, "Category name cannot be empty");
        }
        List<Product> products = repository.findByCategoryName(categoryName);
        if (products.isEmpty()) {
            throw new ApplicationException(Exceptions.NOT_FOUND_EXCEPTION, "No products found or wrong category name");
        }
        return products;
    }

    @Override
    public List<Product> getAllProduct() {
        return repository.findAll();
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
    public List<Product> getFilteringProducts(Float min, Float max, Map<String, String> filterSpec) {
        List<Product> result = new ArrayList<>();
        List<Product> allProducts = repository.findAll();
        List<Product> filter = new ArrayList<>();
        List<String> objectTypeSpecifications = new ArrayList<>();
        if (min != null && max != null) {
            result = allProducts.stream().filter(item -> item.getPrice() >= min && item.getPrice() < max).toList();
        } else {
            result = allProducts;
        }

        if (!filterSpec.isEmpty()) {
            boolean matches = true;
            for (Product product : result) {
                matches = true;
                for (Map.Entry<String, String> entry : filterSpec.entrySet()) {
                    String filterValue = entry.getValue().replace("\"", "").trim();
                    String filterKey = entry.getKey();
                    String nestedKey = "";
                    String[] keyValues;
                    if (filterKey.contains(".")) {
                        keyValues = filterKey.split("\\.");
                        filterKey = keyValues[0];
                        nestedKey = keyValues[keyValues.length - 1];
                        System.out.println(filterKey + " " + nestedKey);
                    }
                    objectTypeSpecifications = specificationRepository.findBySpecificationName(filterKey);
                    System.out.println(objectTypeSpecifications);
                    if (objectTypeSpecifications.contains("Object")) {
                        if (product.getSpecifications().containsKey(filterKey)) {
                            Object productObjectValue = product.getSpecifications().get(filterKey);
                            System.out.println(productObjectValue + " " + "productObjectValue");
                            Object productValue;
                            if (productObjectValue instanceof Map) {
                                Map<String, Object> productValueMap = (Map<String, Object>) productObjectValue;
                                productValue = productValueMap.get(nestedKey);
                                boolean filterBoolean = Boolean.parseBoolean(filterValue);
                                System.out.println("productValue " + productValue);
                                System.out.println("filterValue " + filterValue + " " + ((Object) filterValue).getClass().getName());
                                if (!Objects.equals(productValue, filterBoolean)) {
                                    matches = false;
                                    System.out.println("matches " + matches);
                                }
                            }
                        }
                    } else {
                        if (product.getSpecifications().containsKey(filterKey)) {
                            Object productValue = product.getSpecifications().get(filterKey);
                            if (!Objects.equals(productValue, filterValue)) {
                                matches = false;
                            }
                        }
                    }
                }
                if (matches) {
                    filter.add(product);
                }
            }

            result = filter;
            if (result.isEmpty()) {
                return new ArrayList<>();
            }
        }
        return result;
    }

    @Override
    public Map<String, Map<String, Object>> createPcFilter(Map<String, String> filter) {
        List<Product> products = repository.findAll();
        Map<String,Map<String,Object>>defaultParts = new HashMap<>();
        List<String> categories = List.of("Ram", "Cpu", "Psu", "Gpu", "Ssd", "Hdd", "Case", "Motherboard");

        for (Product product : products) {
            if (categories.contains(product.getCategoryName())) {
                defaultParts.computeIfAbsent(product.getCategoryName(), k -> new HashMap<>()).put(product.getName(), false);
            }
        }
        for (Product product : products) {
            if (categories.contains(product.getCategoryName())) {
                boolean isCompatible = true;
                for (Map.Entry<String, String> entry : filter.entrySet()) {
                    String filterKey = entry.getKey();
                    String filterValue = entry.getValue();
                    Map<String, Object> compatibleObject = (Map<String, Object>) product.getSpecifications().get("compatible");
                    if (compatibleObject.containsKey(filterKey)) {
                        List<String> compatibleValues = (List<String>) compatibleObject.get(filterKey);
                        if (!compatibleValues.contains(filterValue)) {
                            isCompatible = false;
                            break;
                        }
                    }
                }
                if (isCompatible) {
                    defaultParts.get(product.getCategoryName()).put(product.getName(), true);
                }
            }
        }

        return defaultParts;
    }

    @Override
    public List<Product> findRecommendedProduct(RecommendProductRequest request) {
        String usingPurpose = request.getUsingPurpose();
        String whereUse = request.getWhereUse();
        String look = request.getLook();
        List<Product> matchingProducts = repository.findAll().stream()
                .filter(product -> usingPurpose != null && usingPurpose.trim().equalsIgnoreCase(product.getUsingPurpose().trim()))
                .filter(product -> whereUse != null && whereUse.trim().equalsIgnoreCase(product.getWhereUse().trim()))
                .filter(product -> look != null && look.trim().equalsIgnoreCase(product.getLook().trim()))
                .collect(Collectors.toList());
        return matchingProducts;
    }
}

