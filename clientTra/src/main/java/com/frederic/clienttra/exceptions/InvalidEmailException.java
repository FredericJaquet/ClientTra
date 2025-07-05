package com.frederic.clienttra.exceptions;

public class InvalidEmailException extends RuntimeException{
    public InvalidEmailException(){
        super("validation.email.invalid");
    }
}
