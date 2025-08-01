package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when an Iban is invalid.
 * The message key "validation.bank_account.invalid" can be used for localization.
 */
public class InvalidIbanException extends RuntimeException{
    public InvalidIbanException() {
        super("validation.bank_account.invalid");
    }
}
