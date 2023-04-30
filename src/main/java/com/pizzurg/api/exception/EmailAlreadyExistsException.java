package com.pizzurg.api.exception;

public class EmailAlreadyExistsException extends RuntimeException{

    public EmailAlreadyExistsException() {
        super("Este e-mail já está cadastrado.");
    }

}
