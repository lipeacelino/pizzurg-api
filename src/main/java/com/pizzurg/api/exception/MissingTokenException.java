package com.pizzurg.api.exception;

public class MissingTokenException extends RuntimeException{

    public MissingTokenException() {
        super("Token ausente.");
    }

}
