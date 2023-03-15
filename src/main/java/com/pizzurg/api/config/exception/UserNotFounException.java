package com.pizzurg.api.config.exception;

public class UserNotFounException extends RuntimeException {

    public UserNotFounException() {
        super("User not found in system ");
    }

}
