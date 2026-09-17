package com.arya.ecommerce_order_management.service.impl;

import com.arya.ecommerce_order_management.dto.request.CreatePaymentRequest;
import com.arya.ecommerce_order_management.dto.response.PaymentResponse;
import com.arya.ecommerce_order_management.entity.Order;
import com.arya.ecommerce_order_management.entity.Payment;
import com.arya.ecommerce_order_management.entity.enums.OrderStatus;
import com.arya.ecommerce_order_management.exception.DuplicateResourceException;
import com.arya.ecommerce_order_management.exception.ResourceNotFoundException;
import com.arya.ecommerce_order_management.repository.OrderRepository;
import com.arya.ecommerce_order_management.repository.PaymentRepository;
import com.arya.ecommerce_order_management.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request, Long userId){

        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", request.orderId()));

        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only cancel your own orders");
        }

        if (paymentRepository.existsByOrderId(order.getId())) throw new DuplicateResourceException("Order", "id", request.orderId());

        if (order.getStatus() == OrderStatus.CANCELLED ||
                order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException(
                    "Cannot process payment for order with status: "
                            + order.getStatus());
        }

        Payment payment = Payment.builder()
                .order(order)
                .method(request.method())
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        order.setStatus(OrderStatus.CONFIRMED);

        orderRepository.save(order);

        return PaymentResponse.from(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(Long id) {

        Payment payment = paymentRepository.findByOrderId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment","id", id));

        return PaymentResponse.from(payment);
    }
}
