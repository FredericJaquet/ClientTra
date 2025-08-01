package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when the last number from documents is not found in the system.
 * The message "error.last_number.not_found" can be used as a key for localized error messages.
 */
public class LastNumberNotFoundException extends RuntimeException{
    public LastNumberNotFoundException(){
        super("error.last_number.not_found");
    }
}
