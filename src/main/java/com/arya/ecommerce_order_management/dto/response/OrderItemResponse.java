package com.arya.ecommerce_order_management.dto.response;

import com.arya.ecommerce_order_management.entity.OrderItem;
import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal priceAtPurchase,
        BigDecimal itemTotal          // quantity * priceAtPurchase
) {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPriceAtPurchase(),
                item.getPriceAtPurchase()
                        .multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }
}
