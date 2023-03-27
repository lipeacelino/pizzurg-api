package com.pizzurg.api.dto.input.product;

import java.math.BigDecimal;

public record UpdateProductSizeDto(
        String sizeName,
        String description,
        BigDecimal price,
        Boolean available) {
}