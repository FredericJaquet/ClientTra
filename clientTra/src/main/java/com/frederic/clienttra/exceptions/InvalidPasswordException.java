package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a password is invalid.
 * The message key "error.invalid_password" can be used for localization.
 */
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("error.invalid_password");
    }
}
