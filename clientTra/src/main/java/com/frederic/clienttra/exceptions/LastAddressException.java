package com.frederic.clienttra.exceptions;

public class LastAddressException extends RuntimeException{
    public LastAddressException() {
        super("error.address.last_address");
    }
}
