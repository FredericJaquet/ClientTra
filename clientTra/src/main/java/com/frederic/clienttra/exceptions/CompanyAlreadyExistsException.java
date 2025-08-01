package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when attempting to create a company that already exists.
 * Uses the message key "error.company_already_exists" for localization.
 */
public class CompanyAlreadyExistsException extends RuntimeException{
    public CompanyAlreadyExistsException() {
        super("error.company_already_exists");
    }
}
