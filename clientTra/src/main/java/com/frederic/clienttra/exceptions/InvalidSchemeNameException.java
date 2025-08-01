package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a scheme name is invalid.
 * The message key "validation.scheme.scheme_name_required" can be used for localization.
 */
public class InvalidSchemeNameException extends RuntimeException{
    public InvalidSchemeNameException(){
        super("validation.scheme.scheme_name_required");
    }
}
