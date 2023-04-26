package com.pizzurg.api.dto.input.order;

public record CreateDeliveryData(
        String receiverName,
        String address,
        String number,
        String complement,
        String district,
        String zipCode,
        String city,
        String state
) {
}
