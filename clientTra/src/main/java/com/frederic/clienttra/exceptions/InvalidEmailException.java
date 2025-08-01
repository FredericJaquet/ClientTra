package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when an email address is invalid.
 * The message key "validation.email.invalid" can be used for localization.
 */
public class InvalidEmailException extends RuntimeException{
    public InvalidEmailException(){
        super("validation.email.invalid");
    }
}
