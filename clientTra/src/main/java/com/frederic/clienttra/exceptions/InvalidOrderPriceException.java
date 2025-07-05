package com.frederic.clienttra.exceptions;

public class InvalidOrderPriceException extends RuntimeException{
    public InvalidOrderPriceException(){
        super("invalid.order.price_required");
    }
}
