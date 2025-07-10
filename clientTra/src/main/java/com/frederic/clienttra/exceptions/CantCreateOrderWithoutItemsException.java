package com.frederic.clienttra.exceptions;

public class CantCreateOrderWithoutItemsException extends RuntimeException{
    public CantCreateOrderWithoutItemsException(){
        super("error.order.without_item");
    }
}
