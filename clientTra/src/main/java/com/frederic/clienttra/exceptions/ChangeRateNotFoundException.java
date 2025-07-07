package com.frederic.clienttra.exceptions;

public class ChangeRateNotFoundException extends RuntimeException{
    public ChangeRateNotFoundException(){
        super("error.change_rate_not_found");
    }
}
