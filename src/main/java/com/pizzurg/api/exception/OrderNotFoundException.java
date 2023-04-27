package com.pizzurg.api.exception;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException() {
        super("Pedido não encontrado.");
    }

}
