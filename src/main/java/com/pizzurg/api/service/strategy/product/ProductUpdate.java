package com.pizzurg.api.service.strategy.product;

import com.pizzurg.api.dto.input.product.UpdateProductDto;
import com.pizzurg.api.entity.Product;
import org.springframework.stereotype.Component;

public interface ProductUpdate {
    void update(Product product, UpdateProductDto updateProductDto);
}
