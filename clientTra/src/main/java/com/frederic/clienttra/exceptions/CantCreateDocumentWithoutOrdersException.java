package com.frederic.clienttra.exceptions;

public class CantCreateDocumentWithoutOrdersException extends RuntimeException{
    public CantCreateDocumentWithoutOrdersException(){
        super("error.document.without_orders");
    }
}
