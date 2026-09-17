package com.arya.ecommerce_order_management.service;

import com.arya.ecommerce_order_management.dto.request.CreatePaymentRequest;
import com.arya.ecommerce_order_management.dto.response.PaymentResponse;

import java.nio.file.AccessDeniedException;

public interface PaymentService {

    PaymentResponse createPayment(CreatePaymentRequest request, Long userId);
    PaymentResponse getPaymentByOrderId(Long id);

}
