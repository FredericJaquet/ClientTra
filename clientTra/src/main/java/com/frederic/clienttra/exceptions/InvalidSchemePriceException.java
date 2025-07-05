package com.frederic.clienttra.exceptions;

public class InvalidSchemePriceException extends RuntimeException{
    public InvalidSchemePriceException(){
        super("validation.scheme.price");
    }
}
