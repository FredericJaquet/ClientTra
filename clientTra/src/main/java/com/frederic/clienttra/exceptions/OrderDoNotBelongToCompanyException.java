package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when an order does not belong to the expected company.
 */
public class OrderDoNotBelongToCompanyException extends RuntimeException{
    public OrderDoNotBelongToCompanyException(){
        super("error.invoice.order_wrong_company");
    }
}
