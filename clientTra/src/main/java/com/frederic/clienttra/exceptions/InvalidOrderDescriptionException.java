package com.frederic.clienttra.exceptions;

public class InvalidOrderDescriptionException extends RuntimeException{
    public InvalidOrderDescriptionException(){
        super("validation.order.description_required");
    }
}
