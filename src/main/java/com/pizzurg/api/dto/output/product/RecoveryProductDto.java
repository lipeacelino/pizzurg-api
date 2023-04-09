package com.pizzurg.api.dto.output.product;

import lombok.Builder;

import java.util.List;

@Builder
public record RecoveryProductDto(
        Long id,
        String name,
        String description,
        String category,
        List<RecoveryProductVariationDto> productVariations,
        Boolean available ) {
}