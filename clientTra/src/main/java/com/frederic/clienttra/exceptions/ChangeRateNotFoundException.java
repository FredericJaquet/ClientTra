package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a change rate is not found in the system.
 * The message "error.change_rate.not_found" can be used as a key for localized error messages.
 */
public class ChangeRateNotFoundException extends RuntimeException{
    public ChangeRateNotFoundException(){
        super("error.change_rate_not_found");
    }
}
