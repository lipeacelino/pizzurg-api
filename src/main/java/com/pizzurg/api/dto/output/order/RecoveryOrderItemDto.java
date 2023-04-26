package com.pizzurg.api.dto.output.order;

import com.pizzurg.api.dto.output.product.RecoveryProductVariationDto;

public record RecoveryOrderItemDto(
        Long id,

        RecoveryProductVariationDto productVariation,

        Integer quantity

//        RecoveryOrderDto order
) {
}
