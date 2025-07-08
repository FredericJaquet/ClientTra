package com.frederic.clienttra.exceptions;

public class InvalidOrderPriceException extends RuntimeException{
    public InvalidOrderPriceException(){
        super("validation.order.price_required");
    }
}
