package com.frederic.clienttra.exceptions;

public class InvalidIbanException extends RuntimeException{
    public InvalidIbanException() {
        super("error.bank_account.invalid_bank_account");
    }
}
