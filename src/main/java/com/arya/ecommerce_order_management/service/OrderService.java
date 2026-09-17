package com.arya.ecommerce_order_management.service;

import com.arya.ecommerce_order_management.dto.request.CreateOrderRequest;
import com.arya.ecommerce_order_management.dto.response.OrderResponse;
import com.arya.ecommerce_order_management.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;


public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request, Long userId);
    OrderResponse getOrderById(Long orderId);
    PageResponse<OrderResponse> getOrderByUserId(Long userId, Pageable pageable);
    OrderResponse cancelOrder(Long orderId, Long userId);
}
