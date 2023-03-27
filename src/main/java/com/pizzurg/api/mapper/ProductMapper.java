package com.pizzurg.api.mapper;

import com.pizzurg.api.dto.input.product.CreateProductSizeDto;
import com.pizzurg.api.dto.output.product.RecoveryProductDto;
import com.pizzurg.api.dto.output.product.RecoveryProductSizeDto;
import com.pizzurg.api.entity.Product;
import com.pizzurg.api.entity.ProductSize;
import com.pizzurg.api.enums.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "productSizeList", target = "productSizes", qualifiedByName = "recoveryProductSizeDtoListFromProductSizeList")
    @Mapping(source = "category", target = "category", qualifiedByName = "categoryEnumToString")
    RecoveryProductDto recoveryProductDtoFromProduct(Product product);
    @Named("recoveryProductSizeDtoListFromProductSizeList")
    @IterableMapping(qualifiedByName = "recoveryProductSizeDtoFromProductSize")
    List<RecoveryProductSizeDto> recoveryProductSizeDtoListFromProductSizeList(List<ProductSize> productSize);
    @Named("recoveryProductSizeDtoFromProductSize")
    RecoveryProductSizeDto recoveryProductSizeDtoFromProductSize(ProductSize productSize);
    ProductSize productSizeFromCreateProductSizeDto(CreateProductSizeDto createProductSizeDto);
    @Named("categoryEnumToString")
    default String categoryEnumToString(Category category) {
        return category.getName();
    }
}
