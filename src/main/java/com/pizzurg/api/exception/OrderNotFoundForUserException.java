package com.pizzurg.api.exception;

public class OrderNotFoundForUserException extends RuntimeException{

    public OrderNotFoundForUserException() {
        super("Pedido não encontrado para este usuário.");
    }

}
