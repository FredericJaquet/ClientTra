package com.frederic.clienttra.exceptions;

public class LastNumberNotFoundException extends RuntimeException{
    public LastNumberNotFoundException(){
        super("error.last_number.not_found");
    }
}
