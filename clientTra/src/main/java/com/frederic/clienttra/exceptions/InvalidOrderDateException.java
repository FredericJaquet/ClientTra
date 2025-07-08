package com.frederic.clienttra.exceptions;

public class InvalidOrderDateException extends RuntimeException{
    public InvalidOrderDateException(){
        super("validation.order.date_required");
    }
}
