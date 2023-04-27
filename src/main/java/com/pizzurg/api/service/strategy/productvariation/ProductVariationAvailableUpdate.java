package com.pizzurg.api.service.strategy.productvariation;

import com.pizzurg.api.dto.input.product.UpdateProductVariationDto;
import com.pizzurg.api.entity.ProductVariation;
import com.pizzurg.api.exception.ProductVariationUnavailableException;
import org.springframework.stereotype.Component;

@Component
public class ProductVariationAvailableUpdate implements ProductVariationUpdate{
    @Override
    public void update(ProductVariation productVariation, UpdateProductVariationDto updateProductVariationDto) {
        if (updateProductVariationDto.available() != null) {
            if (updateProductVariationDto.available() && !productVariation.getProduct().getAvailable()) {
                throw new ProductVariationUnavailableException();
            }
            productVariation.setAvailable(updateProductVariationDto.available());
        }
    }
}
