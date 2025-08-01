package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a pricing scheme is not found in the system.
 * The message "error.scheme.not_found" can be used as a key for localized error messages.
 */
public class SchemeNotFoundException extends RuntimeException {
    public SchemeNotFoundException() {
        super("error.scheme.not_found");
    }
}
