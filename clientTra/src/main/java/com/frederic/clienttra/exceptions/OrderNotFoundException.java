package com.frederic.clienttra.exceptions;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(){
        super("error.order.not_found");
    }
}
