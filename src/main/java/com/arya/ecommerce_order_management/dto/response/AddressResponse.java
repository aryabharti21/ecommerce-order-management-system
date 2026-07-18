package com.arya.ecommerce_order_management.dto.response;

import com.arya.ecommerce_order_management.entity.Address;

public record AddressResponse(
        Long id,
        String country,
        String state,
        String district,
        String line1,
        String line2,
        String zipcode
) {
    public static AddressResponse from(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getCountry(),
                address.getState(),
                address.getDistrict(),
                address.getLine1(),
                address.getLine2(),
                address.getZipcode()
        );
    }
}