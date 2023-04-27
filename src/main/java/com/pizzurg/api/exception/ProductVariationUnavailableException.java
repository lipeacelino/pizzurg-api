package com.pizzurg.api.exception;

public class ProductVariationUnavailableException extends RuntimeException {

    public ProductVariationUnavailableException() {
        super ("A variação de tamanho não pode estar disponível se o produto estiver indisponível.");
    }

}
