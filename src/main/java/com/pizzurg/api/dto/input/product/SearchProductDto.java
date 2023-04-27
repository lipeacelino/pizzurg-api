package com.pizzurg.api.dto.input.product;

import jakarta.validation.constraints.NotBlank;

public record SearchProductDto(

        @NotBlank(message = "{not.blank.message}")
        String name
) {
}
