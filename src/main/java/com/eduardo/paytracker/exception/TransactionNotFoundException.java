package com.eduardo.paytracker.exception;

import org.springframework.http.HttpStatus;

public class TransactionNotFoundException extends BusinessException {

    public TransactionNotFoundException() {
        super("Transaction not found!", HttpStatus.CONFLICT);
    }
}
