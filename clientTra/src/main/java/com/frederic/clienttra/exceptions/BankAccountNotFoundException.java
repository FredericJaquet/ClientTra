package com.frederic.clienttra.exceptions;

public class BankAccountNotFoundException extends RuntimeException{
    public BankAccountNotFoundException() {
        super("error.bank_account.not_found");
    }
}
