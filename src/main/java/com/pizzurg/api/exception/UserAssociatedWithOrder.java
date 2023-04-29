package com.pizzurg.api.exception;

public class UserAssociatedWithOrder extends RuntimeException {
    public UserAssociatedWithOrder() {
        super("Usuário não pode ser excluído porque está associado a um ou mais pedidos.");
    }
}
