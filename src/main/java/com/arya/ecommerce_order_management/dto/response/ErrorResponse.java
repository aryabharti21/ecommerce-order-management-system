package com.arya.ecommerce_order_management.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timeStamp,
        Map<String, String> fieldErrors
) {
    public ErrorResponse(int status, String message, LocalDateTime timeStamp) {
        this(status, message, timeStamp, null);
    }
}
