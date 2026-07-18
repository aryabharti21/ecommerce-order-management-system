package com.arya.ecommerce_order_management.dto.response;

import com.arya.ecommerce_order_management.entity.User;

public record UserResponse(
        Long id,
        String name,
        String phone,
        String email
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getEmail()
        );
    }
}
