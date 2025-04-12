package com.eduardo.paytracker.exception;

public class ExistentUserException extends RuntimeException {

    public ExistentUserException(String message) {
        super(message);
    }

}
