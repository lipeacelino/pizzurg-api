package com.pizzurg.api.dto.output.order;

public record RecoveryDeliveryData(

        Long id,

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
