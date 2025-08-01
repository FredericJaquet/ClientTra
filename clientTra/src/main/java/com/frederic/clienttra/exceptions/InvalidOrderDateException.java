package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when an order date is invalid.
 * The message key "validation.order.date_required" can be used for localization.
 */
public class InvalidOrderDateException extends RuntimeException{
    public InvalidOrderDateException(){
        super("validation.order.date_required");
    }
}
