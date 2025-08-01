package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a user attempts to access a resource they are not authorized for.
 * The message "error.access_denied" can be used as a key for internationalized error messages.
 */
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("error.access_denied");
    }
}
