package com.electrohub.controller;

import com.electrohub.dto.ApiResponse;
import com.electrohub.dto.CartItemRequestDto;
import com.electrohub.dto.CartItemResponseDto;
import com.electrohub.dto.CartTotalResponseDto;
import com.electrohub.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for Shopping Cart endpoints.
 * Base path: /api/cart
 * Demonstrates Spring MVC REST APIs, JPA persistence, and Constructor-based Dependency Injection.
 */
@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    // Constructor-based Dependency Injection (Spring Core IoC)
    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * POST /api/cart/items — Add a product to the cart.
     */
    @PostMapping("/items")
    public ResponseEntity<CartItemResponseDto> addProductToCart(@Valid @RequestBody CartItemRequestDto requestDto) {
        CartItemResponseDto responseDto = cartService.addProductToCart(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * GET /api/cart/items — Retrieve all cart items.
     */
    @GetMapping("/items")
    public ResponseEntity<List<CartItemResponseDto>> getAllCartItems() {
        List<CartItemResponseDto> items = cartService.getAllCartItems();
        return ResponseEntity.ok(items);
    }

    /**
     * DELETE /api/cart/items/{id} — Remove an item from the cart.
     */
    @DeleteMapping("/items/{id}")
    public ResponseEntity<ApiResponse<Void>> removeCartItem(@PathVariable Long id) {
        cartService.removeCartItem(id);
        return ResponseEntity.ok(ApiResponse.ok("Item with ID " + id + " was removed from the cart."));
    }

    /**
     * GET /api/cart/total — Calculate the cart total price and item count.
     */
    @GetMapping("/total")
    public ResponseEntity<CartTotalResponseDto> getCartTotal() {
        CartTotalResponseDto cartTotal = cartService.calculateCartTotal();
        return ResponseEntity.ok(cartTotal);
    }

    /**
     * DELETE /api/cart/items — Clear all items from the cart.
     */
    @DeleteMapping("/items")
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok(ApiResponse.ok("All items have been cleared from the shopping cart."));
    }
}
