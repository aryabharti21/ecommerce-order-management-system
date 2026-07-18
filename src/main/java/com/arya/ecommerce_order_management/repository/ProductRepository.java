package com.arya.ecommerce_order_management.repository;

import com.arya.ecommerce_order_management.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    List<Product> findByCategoryIdAndPriceBetween(Long categoryId, BigDecimal min, BigDecimal max);

    boolean existsByNameAndCategoryId(String name, Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.category.name = :categoryName")
    List<Product> findByCategoryName(@Param("categoryName") String categoryName);

    @Query(
            value = "SELECT * FROM product WHERE category_id = :categoryId " +
                    "ORDER BY price ASC LIMIT :limit",
            nativeQuery = true
    )
    List<Product> findTopProductsByCategory(
            @Param("categoryId") Long categoryId,
            @Param("limit") int limit
    );
}
