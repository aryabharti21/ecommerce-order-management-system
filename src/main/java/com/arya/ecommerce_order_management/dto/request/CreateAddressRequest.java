package com.arya.ecommerce_order_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAddressRequest(
        @NotBlank String country,
        @NotBlank String state,
        @NotBlank String district,
        @NotBlank String line1,
        String line2,

        @NotBlank @Size(max = 10)
        String zipcode
) {}
