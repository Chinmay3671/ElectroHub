package com.electrohub.controller;

import com.electrohub.dto.ProductRequestDto;
import com.electrohub.dto.ProductResponseDto;
import com.electrohub.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for Product Management endpoints.
 * Base path: /api/products
 * Demonstrates Spring MVC REST APIs and Constructor-based Dependency Injection.
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    // Constructor-based Dependency Injection (Spring Core IoC)
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * POST /api/products — Add a new electronic product.
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> addProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        ProductResponseDto createdProduct = productService.addProduct(requestDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * GET /api/products — Retrieve all electronic products.
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/{id} — Retrieve a specific electronic product by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
}
