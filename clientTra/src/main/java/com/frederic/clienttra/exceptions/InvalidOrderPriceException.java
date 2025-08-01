package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when an order price is invalid.
 * The message key "validation.order.price_required" can be used for localization.
 */
public class InvalidOrderPriceException extends RuntimeException{
    public InvalidOrderPriceException(){
        super("validation.order.price_required");
    }
}
