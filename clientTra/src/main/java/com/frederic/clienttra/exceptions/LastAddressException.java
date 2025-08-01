package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when attempting to delete the last address of a company,
 * which is not allowed.
 * The message key "error.address.last_address" can be used for localization.
 */
public class LastAddressException extends RuntimeException{
    public LastAddressException() {
        super("error.address.last_address");
    }
}
