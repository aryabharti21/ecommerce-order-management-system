package com.arya.ecommerce_order_management.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(
            String resourceName, String fieldName, Object fieldValue) {
        super(String.format(
                "%s already exists with %s : '%s'",
                resourceName, fieldName, fieldValue)
        );
    }
}
