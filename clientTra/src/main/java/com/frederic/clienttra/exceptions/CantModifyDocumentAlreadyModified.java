package com.frederic.clienttra.exceptions;

public class CantModifyDocumentAlreadyModified extends RuntimeException{
    public CantModifyDocumentAlreadyModified(){
        super("error.document.already_modified");
    }
}
