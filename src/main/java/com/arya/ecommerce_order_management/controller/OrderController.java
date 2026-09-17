package com.arya.ecommerce_order_management.controller;

import com.arya.ecommerce_order_management.dto.request.CreateOrderRequest;
import com.arya.ecommerce_order_management.dto.response.OrderResponse;
import com.arya.ecommerce_order_management.dto.response.PageResponse;
import com.arya.ecommerce_order_management.entity.User;
import com.arya.ecommerce_order_management.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "4. Orders",
        description = "Order management. All endpoints require authentication. " +
                "Users can only access their own orders."
)
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Place new order",
            description = "Creates order for authenticated user. " +
                    "Validates address ownership, product availability, " +
                    "and deducts stock automatically."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Order placed successfully"),
            @ApiResponse(responseCode = "404",
                    description = "Product or address not found"),
            @ApiResponse(responseCode = "409",
                    description = "Insufficient stock")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody @Valid CreateOrderRequest request,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(request, currentUser.getId()));
    }

    @Operation(summary = "Get order by ID")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(orderService.getOrderById(currentUser.getId()));
    }

    @Operation(summary = "Get my orders",
            description = "Returns all orders for authenticated user")
    @GetMapping("/my-orders")
    public ResponseEntity<PageResponse<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page, size,
                Sort.by("createdAt").descending());
        // Orders always sorted by newest first
        // No need to expose sort params to user

        return ResponseEntity.ok(
                orderService.getOrderByUserId(
                        currentUser.getId(), pageable));
    }

    @Operation(
            summary = "Cancel order",
            description = "Cancels order and restores inventory. " +
                    "Cannot cancel DELIVERED or already CANCELLED orders."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Order cancelled, stock restored"),
            @ApiResponse(responseCode = "403",
                    description = "Not your order"),
            @ApiResponse(responseCode = "409",
                    description = "Order cannot be cancelled in current status")
    })
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(orderService.cancelOrder(id, currentUser.getId()));
    }
}
