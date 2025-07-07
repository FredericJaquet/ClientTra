package com.frederic.clienttra.exceptions;

public class DocumentNotFoundException extends RuntimeException{
    public DocumentNotFoundException(){
        super("error.document.not_found");
    }
}
