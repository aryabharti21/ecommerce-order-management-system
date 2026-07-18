package com.arya.ecommerce_order_management.dto.response;

import com.arya.ecommerce_order_management.entity.Order;
import com.arya.ecommerce_order_management.entity.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNo,
        String userName,
        String userEmail,
        AddressResponse address,
        OrderStatus status,
        BigDecimal amount,
        List<OrderItemResponse> items,
        LocalDateTime createdAt
) {
    public static OrderResponse from(Order order, List<OrderItemResponse> items) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNo(),
                order.getUser().getName(),
                order.getUser().getEmail(),
                AddressResponse.from(order.getAddress()),
                order.getStatus(),
                order.getAmount(),
                items,
                order.getCreatedAt()
        );
    }
}
