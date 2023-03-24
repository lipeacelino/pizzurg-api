package com.pizzurg.api.dto.output.product;

import com.pizzurg.api.entity.ProductSize;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record RecoveryProductDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String category,
        List<ProductSize> productSizes,
        Boolean available ) {
}