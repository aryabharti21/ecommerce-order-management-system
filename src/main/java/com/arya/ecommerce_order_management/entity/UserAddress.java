package com.arya.ecommerce_order_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserAddress {

    @EmbeddedId
    private UserAddressId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("addressId")
    @JoinColumn(name = "address_id")
    private Address address;
}
