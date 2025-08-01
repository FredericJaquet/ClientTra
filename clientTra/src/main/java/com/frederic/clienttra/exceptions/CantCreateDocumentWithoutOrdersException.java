package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when attempting to create a document without any associated orders.
 * The message "error.document.without_orders" serves as a localization key.
 */
public class CantCreateDocumentWithoutOrdersException extends RuntimeException{
    public CantCreateDocumentWithoutOrdersException(){
        super("error.document.without_orders");
    }
}
