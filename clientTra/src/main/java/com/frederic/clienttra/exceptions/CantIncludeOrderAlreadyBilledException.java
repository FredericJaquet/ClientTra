package com.frederic.clienttra.exceptions;

public class CantIncludeOrderAlreadyBilledException extends RuntimeException{
    public CantIncludeOrderAlreadyBilledException(){
        super("error.invoice.order_already_billed");
    }
}
