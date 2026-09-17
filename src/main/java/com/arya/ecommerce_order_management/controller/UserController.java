package com.arya.ecommerce_order_management.controller;

import com.arya.ecommerce_order_management.dto.request.CreateAddressRequest;
import com.arya.ecommerce_order_management.dto.request.CreateUserRequest;
import com.arya.ecommerce_order_management.dto.response.AddressResponse;
import com.arya.ecommerce_order_management.dto.response.UserResponse;
import com.arya.ecommerce_order_management.entity.User;
import com.arya.ecommerce_order_management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*@PostMapping
    public ResponseEntity<UserResponse> registerUser(
            @RequestBody @Valid CreateUserRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.registerUser(request));
    }*/

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.getUserById(currentUser.getId()));
    }

    @PostMapping("/me/addresses")
    public ResponseEntity<AddressResponse> addAddressToUser(
            @RequestBody @Valid CreateAddressRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.addAddressToUser(currentUser.getId(), request));
    }

    @GetMapping("/me/addresses")
    public ResponseEntity<List<AddressResponse>> getUserAddresses(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.getUserAddresses(currentUser.getId()));
    }
}
