package com.arya.ecommerce_order_management.repository;

import com.arya.ecommerce_order_management.entity.Order;
import com.arya.ecommerce_order_management.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNo(String orderNo);

    List<Order> findByUserId(Long userId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Order> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    // JPQL with JOIN - get orders with user details
    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.user " +       // JOIN FETCH = load user eagerly in same query
            "JOIN FETCH o.address " +    // avoids N+1 query problem
            "WHERE o.user.id = :userId " +
            "ORDER BY o.createdAt DESC")
    List<Order> findOrdersWithDetailsByUserId(@Param("userId") Long userId);
}