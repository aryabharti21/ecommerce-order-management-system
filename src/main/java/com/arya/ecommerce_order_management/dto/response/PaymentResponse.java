package com.arya.ecommerce_order_management.dto.response;

import com.arya.ecommerce_order_management.entity.Payment;
import com.arya.ecommerce_order_management.entity.enums.PaymentMethod;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        String orderNo,
        PaymentMethod method,
        LocalDateTime createdAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getOrderNo(),
                payment.getMethod(),
                payment.getCreatedAt()
        );
    }
}
