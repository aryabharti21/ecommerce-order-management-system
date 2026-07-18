package com.arya.ecommerce_order_management.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductRequest(

    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Name cannot exceed 150 characters")
    String name,

    @NotNull(message = "Category id is required")
    Long categoryId,

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    BigDecimal price,

    String description,

    @NotNull(message = "Initial stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    Integer initialStock
    ) {}

