package com.pizzurg.api.dto.input.order;

import com.pizzurg.api.enums.MethodPayment;

import java.util.List;

public record CreateOrderDto(
        List<CreateOrderItemDto> orderItems,

        MethodPayment methodPayment,

        CreateDeliveryData deliveryData
) {
}
