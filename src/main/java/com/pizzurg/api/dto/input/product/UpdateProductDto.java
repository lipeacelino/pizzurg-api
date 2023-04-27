package com.pizzurg.api.dto.input.product;

public record UpdateProductDto(

        String name,

        String description,

        Boolean available) {
}
