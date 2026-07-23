package com.arya.ecommerce_order_management.service;

import com.arya.ecommerce_order_management.dto.request.CreateAddressRequest;
import com.arya.ecommerce_order_management.dto.request.CreateUserRequest;
import com.arya.ecommerce_order_management.dto.response.AddressResponse;
import com.arya.ecommerce_order_management.dto.response.UserResponse;
import com.arya.ecommerce_order_management.entity.User;

import java.util.List;

public interface UserService {
    UserResponse registerUser(CreateUserRequest request);
    UserResponse getUserById(Long id);
    AddressResponse addAddressToUser(Long userId, CreateAddressRequest request);
    List<AddressResponse> getUserAddresses(Long id);
}
