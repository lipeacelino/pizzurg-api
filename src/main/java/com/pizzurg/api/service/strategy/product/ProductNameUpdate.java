package com.pizzurg.api.service.strategy.product;

import com.pizzurg.api.dto.input.product.UpdateProductDto;
import com.pizzurg.api.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductNameUpdate implements ProductUpdate {
    @Override
    public void update(Product product, UpdateProductDto updateProductDto) {
        if (updateProductDto.name() != null) {
            product.setName(updateProductDto.name());
        }
    }
}
