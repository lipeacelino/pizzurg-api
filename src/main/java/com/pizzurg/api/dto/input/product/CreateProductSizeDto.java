package com.pizzurg.api.dto.input.product;

import com.pizzurg.api.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductSizeDto(
        @NotBlank(message="{not.blank.message}")
        String sizeName,
        @NotBlank(message="{not.blank.message}")
        String description,
        @NotNull(message="{not.null.message}") //é bom validar com um regex
        BigDecimal price,
        Boolean available,
        Product product) {
}
