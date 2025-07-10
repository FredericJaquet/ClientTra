package com.frederic.clienttra.exceptions;

public class InvalidVatRateException extends RuntimeException{
    public InvalidVatRateException(){
        super("validation.document.vat_rate");
    }
}
