package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a customer is not found in the system.
 * The message "error.customer.not_found" can be used as a key for localized error messages.
 */
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException() {
        super("error.customer.not_found");
    }
}
