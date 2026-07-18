package com.arya.ecommerce_order_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        @NotBlank(message = "Category name required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name
        )
{}

