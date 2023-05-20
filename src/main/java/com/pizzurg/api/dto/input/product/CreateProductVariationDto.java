package com.pizzurg.api.dto.input.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductVariationDto(

        @NotBlank(message = "{not.blank.message}")
        String sizeName,

        @NotBlank(message = "{not.blank.message}")
        String description,

        @NotNull(message = "{not.null.message}")
        BigDecimal price,

        @NotNull(message = "{not.null.message}")
        Boolean available

) {
}
