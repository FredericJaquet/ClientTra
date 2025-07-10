package com.frederic.clienttra.exceptions;

public class OrderDoNotBelowToCompanyException extends RuntimeException{
    public OrderDoNotBelowToCompanyException(){
        super("error.invoice.order_wrong_company");
    }
}
