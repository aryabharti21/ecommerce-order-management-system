package com.arya.ecommerce_order_management.repository;

import com.arya.ecommerce_order_management.entity.Inventory;
import com.arya.ecommerce_order_management.entity.enums.InventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);

    List<Inventory> findByStatus(InventoryStatus status);

    boolean existsByProductIdAndStatus(Long productId, InventoryStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.itemCount = i.itemCount - :quantity " +
            "WHERE i.product.id = :productId AND i.itemCount >= :quantity")
    int decreaseStock(
            @Param("productId") Long productId,
            @Param("quantity") int quantity
    );
}