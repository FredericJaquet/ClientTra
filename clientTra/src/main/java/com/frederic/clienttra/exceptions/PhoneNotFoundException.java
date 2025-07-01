package com.frederic.clienttra.exceptions;

public class PhoneNotFoundException extends RuntimeException{
    public PhoneNotFoundException() {
        super("error.phone.not_found");
    }
}
