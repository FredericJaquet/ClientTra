package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a scheme price is invalid.
 * The message key "validation.scheme.price" can be used for localization.
 */
public class InvalidSchemePriceException extends RuntimeException{
    public InvalidSchemePriceException(){
        super("validation.scheme.price");
    }
}
