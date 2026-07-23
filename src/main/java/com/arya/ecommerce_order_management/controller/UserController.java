package com.arya.ecommerce_order_management.controller;

import com.arya.ecommerce_order_management.dto.request.CreateAddressRequest;
import com.arya.ecommerce_order_management.dto.request.CreateUserRequest;
import com.arya.ecommerce_order_management.dto.response.AddressResponse;
import com.arya.ecommerce_order_management.dto.response.UserResponse;
import com.arya.ecommerce_order_management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> registerUser(
            @RequestBody @Valid CreateUserRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.registerUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/{userId}/addresses")
    public ResponseEntity<AddressResponse> addAddressToUser(
            @PathVariable Long userId,
            @RequestBody @Valid CreateAddressRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.addAddressToUser(userId, request));
    }

    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<AddressResponse>> getUserAddresses(
            @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserAddresses(userId));
    }
}
