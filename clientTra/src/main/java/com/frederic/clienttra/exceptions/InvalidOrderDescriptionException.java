package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when an order description is invalid.
 * The message key "validation.order.description_required" can be used for localization.
 */
public class InvalidOrderDescriptionException extends RuntimeException{
    public InvalidOrderDescriptionException(){
        super("validation.order.description_required");
    }
}
