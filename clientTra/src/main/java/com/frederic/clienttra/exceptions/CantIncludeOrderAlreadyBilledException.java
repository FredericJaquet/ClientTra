package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when attempting to include an order that has already been billed in a new document.
 * The message "error.invoice.order_already_billed" is used as a localization key.
 */
public class CantIncludeOrderAlreadyBilledException extends RuntimeException{
    public CantIncludeOrderAlreadyBilledException(){
        super("error.invoice.order_already_billed");
    }
}
