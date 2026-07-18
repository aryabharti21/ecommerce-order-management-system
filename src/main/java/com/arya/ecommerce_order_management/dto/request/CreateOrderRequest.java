package com.arya.ecommerce_order_management.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
//need to check how this nested is working
public record CreateOrderRequest(
        @NotNull(message = "User id is required")
        Long userId,

        @NotNull(message = "Address id is required")
        Long addressId,

        @NotEmpty(message = "Order must have at least one item")
        List<OrderItemRequest> items
) {
    public record OrderItemRequest(
            @NotNull Long productId,
            @Min(value = 1, message = "Quantity must be at least 1") Integer quantity
    ) {}
}
