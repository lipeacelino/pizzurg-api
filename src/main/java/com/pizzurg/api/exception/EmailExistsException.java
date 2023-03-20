package com.pizzurg.api.exception;

public class EmailExistsException extends RuntimeException{

    public EmailExistsException() {
        super("Este e-mail já está cadastrado.");
    }

}
