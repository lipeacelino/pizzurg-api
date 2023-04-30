package com.pizzurg.api.exception;

public class ProductVariationAssociatedWithOrderException extends RuntimeException {

    public ProductVariationAssociatedWithOrderException() {
        super("A variação do produto não pode ser excluído porque está associado a um ou mais pedidos.");
    }

}
