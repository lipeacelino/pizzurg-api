package com.pizzurg.api.dto.input.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

public record CreateOrderDto(

        @NotEmpty(message = "{not.empty.message}")
        List<@Valid CreateOrderItemDto> orderItems,

        @NotBlank(message = "{not.blank.message}")
        String paymentMethod,

        @NotNull(message = "{not.null.message}")
        @Valid
        CreateDeliveryDataDto deliveryData

) {
}
