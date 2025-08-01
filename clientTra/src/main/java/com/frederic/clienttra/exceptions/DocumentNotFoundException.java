package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a document is not found in the system.
 * The message "error.document.not_found" can be used as a key for localized error messages.
 */
public class DocumentNotFoundException extends RuntimeException{
    public DocumentNotFoundException(){
        super("error.document.not_found");
    }
}
