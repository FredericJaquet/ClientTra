package com.frederic.clienttra.exceptions;

public class InvalidWithholdingException extends RuntimeException{
    public InvalidWithholdingException(){
        super("validation.document.withholding");
    }
}
