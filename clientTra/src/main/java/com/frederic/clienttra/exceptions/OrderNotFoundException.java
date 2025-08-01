package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when an order is not found in the system.
 * The message "error.order.not_found" can be used as a key for localized error messages.
 */
public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(){
        super("error.order.not_found");
    }
}
