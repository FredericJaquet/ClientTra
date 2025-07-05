package com.frederic.clienttra.exceptions;

public class InvalidVatNumberException extends RuntimeException{
    public InvalidVatNumberException(){
        super("validation.company.vat_number_required");
    }
}
