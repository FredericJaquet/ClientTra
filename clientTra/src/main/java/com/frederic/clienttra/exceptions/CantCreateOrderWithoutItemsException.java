package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when attempting to create an order without any associated items.
 * The message "error.order.without_item" serves as a localization key.
 */
public class CantCreateOrderWithoutItemsException extends RuntimeException{
    public CantCreateOrderWithoutItemsException(){
        super("error.order.without_item");
    }
}
