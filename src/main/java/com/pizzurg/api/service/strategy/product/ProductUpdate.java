package com.pizzurg.api.service.strategy.product;

import com.pizzurg.api.dto.input.product.UpdateProductDto;
import com.pizzurg.api.entity.Product;

public interface ProductUpdate {
    void update(Product product, UpdateProductDto updateProductDto);
}
