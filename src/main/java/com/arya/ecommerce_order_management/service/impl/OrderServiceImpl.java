package com.arya.ecommerce_order_management.service.impl;

import com.arya.ecommerce_order_management.dto.request.CreateOrderRequest;
import com.arya.ecommerce_order_management.dto.response.OrderItemResponse;
import com.arya.ecommerce_order_management.dto.response.OrderResponse;
import com.arya.ecommerce_order_management.dto.response.PageResponse;
import com.arya.ecommerce_order_management.entity.*;
import com.arya.ecommerce_order_management.entity.enums.OrderStatus;
import com.arya.ecommerce_order_management.exception.InsufficientStockException;
import com.arya.ecommerce_order_management.exception.ResourceNotFoundException;
import com.arya.ecommerce_order_management.repository.*;
import com.arya.ecommerce_order_management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!userAddressRepository.existsByUser_IdAndId_AddressId(userId, request.addressId())) {
            throw new ResourceNotFoundException("Address", "id", request.addressId());
        }

        //storing the product after validation in map for further processing
        Map<Long, Product> productMap = new HashMap<>();

        BigDecimal total =  BigDecimal.ZERO;

        for (CreateOrderRequest.OrderItemRequest item : request.items()){
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", item.productId()));

            Inventory inventory = inventoryRepository.findByProductId(item.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", item.productId()));

            if (inventory.getItemCount() < item.quantity()){
                throw new InsufficientStockException(product.getName(), item.quantity(), inventory.getItemCount());
            }

            productMap.put(item.productId(), product);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(item.quantity())));
        }

        String orderNo = "ORD-"+UUID.randomUUID().toString().substring(0,8).toUpperCase();

        Address address = addressRepository.findById(request.addressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", request.addressId()));

        Order order = Order.builder()
                .orderNo(orderNo)
                .user(user)
                .address(address)
                .status(OrderStatus.PLACED)
                .amount(total)
                .build();

        Order savedOrder = orderRepository.save(order);

        for (CreateOrderRequest.OrderItemRequest item : request.items()){

            Product product = productMap.get(item.productId());

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .product(product)
                    .quantity(item.quantity())
                    .priceAtPurchase(product.getPrice()) // current price of the product at the time of purchase
                    .build();

            orderItemRepository.save(orderItem);

            int updated = inventoryRepository.decreaseStock(
                    item.productId(), item.quantity());
            if (updated == 0){
                throw new InsufficientStockException(product.getName());
            }
        }
        List<OrderItem> savedItems = orderItemRepository.findByOrderId(savedOrder.getId());

        List<OrderItemResponse> itemResponses = savedItems.stream()
                .map(OrderItemResponse::from)
                .toList();

        return OrderResponse.from(savedOrder, itemResponses);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        List<OrderItemResponse> itemResponses = orderItemRepository
                .findByOrderId(orderId)
                .stream()
                .map(OrderItemResponse::from)
                .toList();

        return OrderResponse.from(order,itemResponses);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getOrderByUserId(Long userId, Pageable pageable) {

        if (!userRepository.existsById(userId)) throw new ResourceNotFoundException("User", "id", userId);

        Page<Order> orderPage = orderRepository.findByUserId(userId, pageable);

        Page<OrderResponse> responsePage = orderPage
                .map(order -> {
                    List<OrderItemResponse> itemResponses =
                            orderItemRepository.findByOrderId(order.getId())
                                    .stream()
                                    .map(OrderItemResponse::from)
                                    .toList();
                    return OrderResponse.from(order, itemResponses);
                });

        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId, Long userId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        //checking if order belongs to this user
        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only cancel your own orders");
        }

        if (order.getStatus() == OrderStatus.DELIVERED ||
            order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order cannot be cancelled. Current status: "+order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

        //repopulating the stock
        for (OrderItem orderItem : items) {
            inventoryRepository.increaseStock(orderItem.getProduct().getId(), orderItem.getQuantity());
        }

        List<OrderItemResponse> orderItemResponses = items.stream()
                .map(OrderItemResponse::from)
                .toList();

        return OrderResponse.from(savedOrder, orderItemResponses);
    }
}
