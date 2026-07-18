package com.arya.ecommerce_order_management.exception;

public class InsufficientStockException extends RuntimeException{

    public InsufficientStockException(String productname, int requested, int available) {
        super(String.format("Insufficient stock for product '%s'. Requested: %d, Available: %d",
                productname, requested, available));
    }
}
