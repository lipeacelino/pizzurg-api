package com.pizzurg.api.dto.input.order;

public record CreateOrderItemDto(
        Long productId,
        Long productVariationId,
        Integer quantity
) {
}
