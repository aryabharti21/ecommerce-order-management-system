package com.arya.ecommerce_order_management.service;

import com.arya.ecommerce_order_management.dto.request.RegisterRequest;
import com.arya.ecommerce_order_management.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
}
