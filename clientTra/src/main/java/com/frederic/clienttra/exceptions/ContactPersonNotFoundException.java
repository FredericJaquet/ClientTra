package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a contact person is not found in the system.
 * The message "error.contact_person.not_found" can be used as a key for localized error messages.
 */
public class ContactPersonNotFoundException extends RuntimeException{
    public ContactPersonNotFoundException() {
        super("error.contact_person.not_found");
    }
}
