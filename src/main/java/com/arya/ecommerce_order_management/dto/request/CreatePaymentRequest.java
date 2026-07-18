package com.arya.ecommerce_order_management.dto.request;

import com.arya.ecommerce_order_management.entity.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(
        @NotNull(message = "Order id is required")
        Long orderId,

        @NotNull(message = "Payment method is required")
        PaymentMethod method
) {}
