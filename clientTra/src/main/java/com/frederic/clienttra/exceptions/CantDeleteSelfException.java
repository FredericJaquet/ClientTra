package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when an admin user attempts to delete their own account.
 * The message "error.user.admin_cannot_delete_self" is used as a localization key.
 */
public class CantDeleteSelfException extends RuntimeException{
    public CantDeleteSelfException() {
        super("error.user.admin_cannot_delete_self");
    }
}
