package com.pizzurg.api.exception;

public class ProductAssociatedWithOrderException extends RuntimeException {

    public ProductAssociatedWithOrderException() {
        super("Produto não pode ser excluído porque está associado a um ou mais pedidos.");
    }

}
