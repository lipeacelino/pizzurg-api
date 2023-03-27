package com.pizzurg.api.dto.output.product;

import java.math.BigDecimal;

public record RecoveryProductSizeDto(
        Long id,
        String sizeName,
        String description,
        BigDecimal price,
        Boolean available) {
}
