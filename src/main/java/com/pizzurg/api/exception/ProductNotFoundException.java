package com.pizzurg.api.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("O produto não existe.");
    }
}
