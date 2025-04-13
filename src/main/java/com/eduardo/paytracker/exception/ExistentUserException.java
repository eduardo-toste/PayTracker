package com.eduardo.paytracker.exception;

import org.springframework.http.HttpStatus;

public class ExistentUserException extends BusinessException {

    public ExistentUserException() {
        super("This user is already registered!", HttpStatus.CONFLICT);
    }

}
