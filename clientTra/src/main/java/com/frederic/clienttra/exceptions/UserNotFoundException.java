package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a user is not found in the system.
 * The message "error.user.not_found" can be used as a key for localized error messages.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("error.user.not_found");
    }
}
