package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a VAT number is invalid.
 * The message key "validation.company.vat_number_required" can be used for localization.
 */
public class InvalidVatNumberException extends RuntimeException{
    public InvalidVatNumberException(){
        super("validation.company.vat_number_required");
    }
}
