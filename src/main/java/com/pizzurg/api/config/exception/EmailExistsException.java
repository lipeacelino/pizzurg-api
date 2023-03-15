package com.pizzurg.api.config.exception;

public class EmailExistsException extends RuntimeException{

    public EmailExistsException() {
        super("This email has already been registered in the system");
    }

}
