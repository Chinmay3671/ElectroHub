package com.electrohub.service;

import com.electrohub.dto.CartItemRequestDto;
import com.electrohub.dto.CartItemResponseDto;
import com.electrohub.dto.CartTotalResponseDto;
import com.electrohub.entity.CartItem;
import com.electrohub.entity.Product;
import com.electrohub.exception.ResourceNotFoundException;
import com.electrohub.repository.CartItemRepository;
import com.electrohub.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for Shopping Cart business logic.
 * Demonstrates Spring Core Constructor Injection and JPA persistence.
 */
@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    // Constructor-based Dependency Injection (Spring Core IoC)
    @Autowired
    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    /**
     * Adds an electronic product to the shopping cart.
     * If the product already exists in the cart, increments the existing quantity.
     */
    @Transactional
    public CartItemResponseDto addProductToCart(CartItemRequestDto requestDto) {
        // Validate that the product exists in the catalog
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + requestDto.getProductId()));

        // Check if this product is already in the cart
        Optional<CartItem> existingItemOpt = cartItemRepository.findByProductId(product.getId());

        CartItem cartItem;
        if (existingItemOpt.isPresent()) {
            cartItem = existingItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());
        } else {
            cartItem = new CartItem(product, requestDto.getQuantity());
        }

        CartItem savedItem = cartItemRepository.save(cartItem);
        return mapToResponseDto(savedItem);
    }

    /**
     * Retrieves all items currently stored in the cart.
     */
    @Transactional(readOnly = true)
    public List<CartItemResponseDto> getAllCartItems() {
        return cartItemRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Removes an item from the cart by its CartItem ID.
     */
    @Transactional
    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with ID: " + cartItemId));

        cartItemRepository.delete(cartItem);
    }

    /**
     * Calculates the grand total price and item counts of all cart items.
     */
    @Transactional(readOnly = true)
    public CartTotalResponseDto calculateCartTotal() {
        List<CartItem> items = cartItemRepository.findAll();

        int totalQuantity = 0;
        double grandTotal = 0.0;

        List<CartItemResponseDto> dtoList = items.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());

        for (CartItem item : items) {
            totalQuantity += item.getQuantity();
            grandTotal += item.getSubtotal();
        }

        // Round to 2 decimal places
        double roundedTotal = Math.round(grandTotal * 100.0) / 100.0;

        return new CartTotalResponseDto(
                totalQuantity,
                dtoList.size(),
                roundedTotal,
                dtoList
        );
    }

    /**
     * Clears all items from the cart.
     */
    @Transactional
    public void clearCart() {
        cartItemRepository.deleteAll();
    }

    /**
     * Helper method to map JPA CartItem entity to CartItemResponseDto.
     */
    private CartItemResponseDto mapToResponseDto(CartItem cartItem) {
        Product product = cartItem.getProduct();
        return new CartItemResponseDto(
                cartItem.getId(),
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                cartItem.getQuantity(),
                Math.round(cartItem.getSubtotal() * 100.0) / 100.0
        );
    }
}
