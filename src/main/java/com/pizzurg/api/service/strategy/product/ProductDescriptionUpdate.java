package com.pizzurg.api.service.strategy.product;

import com.pizzurg.api.dto.input.product.UpdateProductDto;
import com.pizzurg.api.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDescriptionUpdate implements ProductUpdate {
    @Override
    public void update(Product product, UpdateProductDto updateProductDto) {
        if (updateProductDto.description() != null) {
            product.setDescription(updateProductDto.description());
        }
    }
}
