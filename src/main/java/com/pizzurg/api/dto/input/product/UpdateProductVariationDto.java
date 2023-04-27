package com.pizzurg.api.dto.input.product;

import java.math.BigDecimal;

public record UpdateProductVariationDto(

        String sizeName,

        String description,

        BigDecimal price,

        Boolean available) {
}