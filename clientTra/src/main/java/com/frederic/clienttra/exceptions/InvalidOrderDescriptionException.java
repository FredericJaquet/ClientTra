package com.frederic.clienttra.exceptions;

public class InvalidOrderDescriptionException extends RuntimeException{
    public InvalidOrderDescriptionException(){
        super("invalid.order.description_required");
    }
}
