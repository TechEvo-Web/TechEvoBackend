package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.dto.request.ProductRequest;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.mapper.ProductMapper;
import com.backend.ecommercebackend.model.product.Product;
import com.backend.ecommercebackend.repository.product.CommentRepository;
import com.backend.ecommercebackend.repository.product.ProductRepository;
import com.backend.ecommercebackend.service.FileStorageService;
import com.backend.ecommercebackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository repository;
    private final FileStorageService fileStorageService;
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;


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
    public List<Product> getAllProducts() {
        return productRepository.findAll();
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


    private void addImage(List<MultipartFile> imageFiles, Product product, List<String> imageUrls) {
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
}

