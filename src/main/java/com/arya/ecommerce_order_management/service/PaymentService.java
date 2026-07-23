package com.arya.ecommerce_order_management.service;

import com.arya.ecommerce_order_management.dto.request.CreatePaymentRequest;
import com.arya.ecommerce_order_management.dto.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(CreatePaymentRequest request);
    PaymentResponse getPaymentByOrderId(Long id);

}
