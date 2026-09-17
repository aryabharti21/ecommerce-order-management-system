package com.arya.ecommerce_order_management.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Schema(description = "Full name",
                example = "Arya Bharti")
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,

        @Schema(description = "Valid email address",
                example = "arya@gmail.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 150)
        String email,

        @Schema(description = "10 digit mobile number",
                example = "9876543210")
        @NotBlank(message = "Phone is required")
        @Pattern(
                regexp = "^[0-9]{10}$",
                message = "Phone must be 10 digits"
        )
        String phone,

        @Schema(description = "Minimum 8 characters",
                example = "password123")
        @NotBlank(message = "Password is required")
        @Size(
                min = 8,
                message = "Password must be at least 8 characters"
        )
        String password
) {}
