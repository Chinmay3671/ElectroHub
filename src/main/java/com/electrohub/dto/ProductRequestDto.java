package com.electrohub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating/adding an electronic product.
 */
public class ProductRequestDto {

    @NotBlank(message = "Product name cannot be empty")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    private String name;

    @NotBlank(message = "Product category cannot be empty")
    @Size(min = 2, max = 100, message = "Category must be between 2 and 100 characters")
    private String category;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    public ProductRequestDto() {
    }

    public ProductRequestDto(String name, String category, Double price, String description) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
