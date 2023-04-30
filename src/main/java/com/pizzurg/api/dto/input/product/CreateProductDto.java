package com.pizzurg.api.dto.input.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateProductDto(

        @NotBlank(message = "{not.blank.message}")
        String name,

        @NotBlank(message = "{not.blank.message}")
        String description,

        @NotBlank(message = "{not.blank.message}")
        String category,

        @NotEmpty(message = "{not.empty.message}")
        List<@Valid CreateProductVariationDto> productVariations,

        @NotNull(message = "{not.null.message}")
        Boolean available

) {
}
