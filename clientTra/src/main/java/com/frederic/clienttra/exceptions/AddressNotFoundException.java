package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when an address is not found in the system.
 * The message "error.address.not_found" can be used as a key for localized error messages.
 */
public class AddressNotFoundException extends RuntimeException{
    public AddressNotFoundException() {
        super("error.address.not_found");
    }
}
