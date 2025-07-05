package com.frederic.clienttra.exceptions;

public class InvalidLegalNameException extends RuntimeException{
    public InvalidLegalNameException(){
        super("validation.company.legal_name_required");
    }
}
