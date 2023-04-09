package com.pizzurg.api.dto.input.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateProductDto(
        @NotBlank(message="{not.blank.message}")
        String name,
        @NotBlank(message="{not.blank.message}")
        String description,
        @NotBlank(message="{not.blank.message}")
        String category,
        @NotEmpty(message = "{not.empty.message}")
        List<CreateProductVariationDto> productVariations,
        Boolean available ) {
}
