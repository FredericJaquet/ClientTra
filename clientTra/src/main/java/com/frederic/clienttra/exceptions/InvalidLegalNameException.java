package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a legal name of a company is invalid.
 * The message key "validation.company.legal_name_required" can be used for localization.
 */
public class InvalidLegalNameException extends RuntimeException{
    public InvalidLegalNameException(){
        super("validation.company.legal_name_required");

    }
}
