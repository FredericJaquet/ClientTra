package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a company is not found in the system.
 * The message "error.company.not_found" can be used as a key for localized error messages.
 */
public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException() {
        super("error.company.not_found");
    }
}
