package com.pizzurg.api.exception;

public class OrderNotFoundByUserException extends RuntimeException{

    public OrderNotFoundByUserException() {
        super("Pedido não encontrado para este usuário.");
    }

}
