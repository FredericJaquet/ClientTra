package com.frederic.clienttra.exceptions;

public class InvalidOrderDateException extends RuntimeException{
    public InvalidOrderDateException(){
        super("invalid.order.date_required");
    }
}
