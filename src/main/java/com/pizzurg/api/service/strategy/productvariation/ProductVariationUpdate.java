package com.pizzurg.api.service.strategy.productvariation;

import com.pizzurg.api.dto.input.product.UpdateProductVariationDto;
import com.pizzurg.api.entity.ProductVariation;
import org.springframework.stereotype.Component;

@Component
public interface ProductVariationUpdate {
    void update(ProductVariation productVariation, UpdateProductVariationDto updateProductVariationDto);
}
