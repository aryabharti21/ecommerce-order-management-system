package com.arya.ecommerce_order_management.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        /*@NotNull(message = "User id is required")
        Long userId,*/

        @Schema(description = "ID of delivery address",
                example = "1")
        @NotNull(message = "Address id is required")
        Long addressId,

        @Schema(description = "List of products to order")
        @NotEmpty(message = "Order must have at least one item")
        List<@Valid OrderItemRequest> items
) {
    public record OrderItemRequest(
            @Schema(description = "Product ID", example = "1")
            @NotNull
            Long productId,

            @Schema(description = "Quantity to order",
                    example = "2",
                    minimum = "1")
            @NotNull(message = "Quantity is required")
            @Min(value = 1, message = "Quantity must be at least 1")
            Integer quantity
    ) {}
}
