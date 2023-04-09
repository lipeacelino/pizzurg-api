package com.pizzurg.api.service.strategy.product;

import com.pizzurg.api.dto.input.product.UpdateProductDto;
import com.pizzurg.api.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductAvailableUpdate implements ProductUpdate{
    @Override
    public void update(Product product, UpdateProductDto updateProductDto) {
        if (updateProductDto.available() != null) {
            product.setAvailable(updateProductDto.available());

            /*se o product estiver com o available = false, todos os productSizes devem ser setados com available false também,
            porque não faria sentido o produto estar estar indisponível e os tamanhos daquele produto estarem disponíveis*/
            if (!product.getAvailable()) {
                product.getProductVariationList().forEach(productSize -> productSize.setAvailable(false));
            }
        }
    }
}
