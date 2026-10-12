package com.electrohub.dto;

import java.util.List;

/**
 * Response DTO for Cart total calculation.
 */
public class CartTotalResponseDto {

    private Integer totalQuantity;
    private Integer totalItems;
    private Double grandTotal;
    private List<CartItemResponseDto> items;

    public CartTotalResponseDto() {
    }

    public CartTotalResponseDto(Integer totalQuantity, Integer totalItems, Double grandTotal, List<CartItemResponseDto> items) {
        this.totalQuantity = totalQuantity;
        this.totalItems = totalItems;
        this.grandTotal = grandTotal;
        this.items = items;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public List<CartItemResponseDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponseDto> items) {
        this.items = items;
    }
}
