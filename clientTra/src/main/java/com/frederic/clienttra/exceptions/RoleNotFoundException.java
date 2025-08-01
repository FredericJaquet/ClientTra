package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a role is not found in the system.
 * The message "error.role.not_found" can be used as a key for localized error messages.
 */
public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException() {
        super("error.role.not_found");
    }
}
