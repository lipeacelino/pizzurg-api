package com.pizzurg.api.mapper;

import com.pizzurg.api.dto.output.product.RecoveryProductDto;
import com.pizzurg.api.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "productSizeList", target = "productSizes")
    public RecoveryProductDto recoveryProdDtoToProd(Product product);
}
