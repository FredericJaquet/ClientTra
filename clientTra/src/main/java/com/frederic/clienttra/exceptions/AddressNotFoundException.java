package com.frederic.clienttra.exceptions;

public class AddressNotFoundException extends RuntimeException{
    public AddressNotFoundException() {
        super("error.address.not_found");
    }
}
