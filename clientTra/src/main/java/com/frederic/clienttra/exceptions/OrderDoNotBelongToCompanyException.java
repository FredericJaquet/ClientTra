package com.frederic.clienttra.exceptions;

public class OrderDoNotBelongToCompanyException extends RuntimeException{
    public OrderDoNotBelongToCompanyException(){
        super("error.invoice.order_wrong_company");
    }
}
