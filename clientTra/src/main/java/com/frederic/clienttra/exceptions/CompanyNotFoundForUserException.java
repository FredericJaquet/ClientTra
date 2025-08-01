package com.frederic.clienttra.exceptions;

public class CompanyNotFoundForUserException extends RuntimeException {

    /**
     * Exception thrown when a company associated with a user is not found.
     * Uses a default localized message key "error.company.not_found_for_user".
     */
    public CompanyNotFoundForUserException(String message) {
        super(message);
    }
    public CompanyNotFoundForUserException() {
        super("error.company.not_found_for_user");
    }
}
