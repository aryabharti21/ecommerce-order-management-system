package com.arya.ecommerce_order_management.dto.response;

import com.arya.ecommerce_order_management.entity.User;

public record LoginResponse(
        String token,
        Long userId,
        String name,
        String email,
        String role
) {
    public static LoginResponse from(String token, User user){
        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
