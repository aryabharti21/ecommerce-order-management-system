package com.arya.ecommerce_order_management.controller;

import com.arya.ecommerce_order_management.dto.request.LoginRequest;
import com.arya.ecommerce_order_management.dto.request.RegisterRequest;
import com.arya.ecommerce_order_management.dto.response.ErrorResponse;
import com.arya.ecommerce_order_management.dto.response.LoginResponse;
import com.arya.ecommerce_order_management.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "1. Authentication",
        description = "Register and login. " +
                "No token required for these endpoints."
)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register new user",
            description = "Creates a new user account and returns JWT token. " +
                    "Use this token for all authenticated requests."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(schema = @Schema(
                            implementation = LoginResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed (invalid email, weak password)",
                    content = @Content(schema = @Schema(
                            implementation = ErrorResponse.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email or phone already registered",
                    content = @Content(schema = @Schema(
                            implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody @Valid RegisterRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @Operation(
            summary = "Login",
            description = "Authenticate with email and password. " +
                    "Returns JWT token valid for 15 minutes."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid email or password")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
