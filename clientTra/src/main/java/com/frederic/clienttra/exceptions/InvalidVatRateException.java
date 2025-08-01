package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a VAT rate is invalid.
 * The message key "validation.document.vat_rate" can be used for localization.
 */
public class InvalidVatRateException extends RuntimeException{
    public InvalidVatRateException(){
        super("validation.document.vat_rate");
    }
}
