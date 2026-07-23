package com.arya.ecommerce_order_management.repository;

import com.arya.ecommerce_order_management.entity.UserAddress;
import com.arya.ecommerce_order_management.entity.UserAddressId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, UserAddressId> {

    List<UserAddress> findByUserId(Long userId);

    boolean existsByUserIdAndId_AddressId(Long userId, Long addressId);

}