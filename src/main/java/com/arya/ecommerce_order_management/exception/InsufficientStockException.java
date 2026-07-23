package com.arya.ecommerce_order_management.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String productName, int requested, int available) {
        super(String.format(
                "Insufficient stock for '%s'. Requested: %d, Available: %d",
                productName, requested, available
        ));
    }

    public InsufficientStockException(String productName) {
        super(String.format(
                "Product '%s' went out of stock. Please try again.",
                productName
        ));
    }
}
