package com.pizzurg.api.dto.input.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderDto(

        @NotEmpty(message = "{not.empty.message}")
        List<CreateOrderItemDto> orderItems,

        @NotBlank(message = "{not.blank.message}")
        String methodPayment,

        @NotNull(message = "{not.null.message}")
        CreateDeliveryDataDto deliveryData
) {
}
