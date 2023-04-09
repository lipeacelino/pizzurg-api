package com.pizzurg.api.mapper;

import com.pizzurg.api.dto.input.product.CreateProductVariationDto;
import com.pizzurg.api.dto.output.product.RecoveryProductDto;
import com.pizzurg.api.dto.output.product.RecoveryProductVariationDto;
import com.pizzurg.api.entity.Product;
import com.pizzurg.api.entity.ProductVariation;
import com.pizzurg.api.enums.Category;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "productVariationList", target = "productVariations", qualifiedByName = "recoveryProductVariationDtoListFromProductVariationList")
    @Mapping(source = "category", target = "category", qualifiedByName = "categoryEnumToString")
    RecoveryProductDto recoveryProductDtoFromProduct(Product product);
    @Named("recoveryProductVariationDtoListFromProductVariationList")
    @IterableMapping(qualifiedByName = "recoveryProductVariationDtoFromProductVariation")
    List<RecoveryProductVariationDto> recoveryProductVariationDtoListFromProductVariationList(List<ProductVariation> productVariation);
    @Named("recoveryProductVariationDtoFromProductVariation")
    RecoveryProductVariationDto recoveryProductVariationDtoFromProductVariation(ProductVariation productVariation);
    ProductVariation productVariationFromCreateProductVariationDto(CreateProductVariationDto createProductVariationDto);
    @Named("categoryEnumToString")
    default String categoryEnumToString(Category category) {
        return category.getName();
    }
}
