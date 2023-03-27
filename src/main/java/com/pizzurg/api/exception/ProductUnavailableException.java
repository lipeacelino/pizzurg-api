package com.pizzurg.api.exception;

public class ProductUnavailableException extends RuntimeException {
    public ProductUnavailableException() {
        super ("Esta variação de tamanho não pode estar disponível se o produto estiver indisponível.");
    }
}
