package com.pizzurg.api.dto.input.order;

import jakarta.validation.constraints.NotNull;

public record CreateOrderItemDto(

        @NotNull(message = "{not.null.message}")
        Long productId,

        @NotNull(message = "{not.null.message}")
        Long productVariationId,

        @NotNull(message = "{not.null.message}")
        Integer quantity
) {
}
