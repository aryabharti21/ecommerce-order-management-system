package com.arya.ecommerce_order_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable // This class is embedded inside UserAddress entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserAddressId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "address_id")
    private Long addressId;

}
