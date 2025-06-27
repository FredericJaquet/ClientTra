package com.frederic.clienttra.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException() {
        super("error.customer.not_found");
    }
}
