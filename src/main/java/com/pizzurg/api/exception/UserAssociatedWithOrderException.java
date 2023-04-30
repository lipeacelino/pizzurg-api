package com.pizzurg.api.exception;

public class UserAssociatedWithOrderException extends RuntimeException {
    public UserAssociatedWithOrderException() {
        super("Usuário não pode ser excluído porque está associado a um ou mais pedidos.");
    }
}
