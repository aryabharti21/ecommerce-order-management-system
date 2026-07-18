package com.arya.ecommerce_order_management.dto.response;

import com.arya.ecommerce_order_management.entity.Category;
import java.time.LocalDateTime;

public record CategoryResponse(
        Long id,
        String name,
        LocalDateTime createdAt
) {
    // Static factory method - converts Entity to DTO
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getCreatedAt()
        );
    }
}
