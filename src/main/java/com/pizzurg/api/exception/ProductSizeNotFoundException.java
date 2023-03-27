package com.pizzurg.api.exception;

public class ProductSizeNotFoundException extends RuntimeException {
    public ProductSizeNotFoundException() {
        super("Tamanho do produto não encontrado.");
    }
}