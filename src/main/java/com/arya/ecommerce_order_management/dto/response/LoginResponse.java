package com.arya.ecommerce_order_management.dto.response;

public record LoginResponse(
        String token,
        Long userId,
        String name,
        String email,
        String role
) {}
