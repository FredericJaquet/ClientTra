package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when attempting to modify a document that has already been marked as modified.
 * The message "error.document.already_modified" is used as a localization key.
 */
public class CantModifyDocumentAlreadyModified extends RuntimeException{
    public CantModifyDocumentAlreadyModified(){
        super("error.document.already_modified");
    }
}
