package com.electrohub.service;

import com.electrohub.dto.ProductRequestDto;
import com.electrohub.dto.ProductResponseDto;
import com.electrohub.entity.Product;
import com.electrohub.exception.ResourceNotFoundException;
import com.electrohub.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Product business logic.
 * Demonstrates Spring Core Constructor Injection and Transaction Management.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Constructor-based Dependency Injection (Spring Core IoC)
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Adds a new electronic product to the catalog.
     */
    @Transactional
    public ProductResponseDto addProduct(ProductRequestDto requestDto) {
        Product product = new Product(
                requestDto.getName().trim(),
                requestDto.getCategory().trim(),
                requestDto.getPrice(),
                requestDto.getDescription() != null ? requestDto.getDescription().trim() : null
        );

        Product savedProduct = productRepository.save(product);
        return mapToResponseDto(savedProduct);
    }

    /**
     * Retrieves all products from the catalog.
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single product by its unique ID.
     */
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return mapToResponseDto(product);
    }

    /**
     * Helper method to map JPA Product entity to ProductResponseDto.
     */
    private ProductResponseDto mapToResponseDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getDescription()
        );
    }
}
