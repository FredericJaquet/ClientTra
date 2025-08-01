package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a phone is not found in the system.
 * The message "error.phone.not_found" can be used as a key for localized error messages.
 */
public class PhoneNotFoundException extends RuntimeException{
    public PhoneNotFoundException() {
        super("error.phone.not_found");
    }
}
