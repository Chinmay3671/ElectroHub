package com.electrohub.dto;

/**
 * Response DTO for a cart item, including product details and subtotal.
 */
public class CartItemResponseDto {

    private Long id;
    private Long productId;
    private String productName;
    private String category;
    private Double unitPrice;
    private Integer quantity;
    private Double subtotal;

    public CartItemResponseDto() {
    }

    public CartItemResponseDto(Long id, Long productId, String productName, String category,
                               Double unitPrice, Integer quantity, Double subtotal) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
