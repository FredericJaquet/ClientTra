package com.frederic.clienttra.exceptions;

public class ContactPersonNotFoundException extends RuntimeException{
    public ContactPersonNotFoundException() {
        super("error.contact_person.not_found");
    }
}
