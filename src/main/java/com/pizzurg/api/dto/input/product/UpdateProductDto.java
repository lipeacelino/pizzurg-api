package com.pizzurg.api.dto.input.product;

import java.math.BigDecimal;

public record UpdateProductDto(
        String name,
        String description,
        BigDecimal price,
        Boolean available) {
}
