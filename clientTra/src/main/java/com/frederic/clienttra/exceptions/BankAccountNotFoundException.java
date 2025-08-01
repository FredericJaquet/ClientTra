package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a bank account is not found in the system.
 * The message "error.bank_account.not_found" can be used as a key for localized error messages.
 */
public class BankAccountNotFoundException extends RuntimeException{
    public BankAccountNotFoundException() {
        super("error.bank_account.not_found");
    }
}
