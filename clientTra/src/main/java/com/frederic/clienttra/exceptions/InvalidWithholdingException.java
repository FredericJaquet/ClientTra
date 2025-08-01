package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a Withholding rate is invalid.
 * The message key "validation.document.Withholding" can be used for localization.
 */
public class InvalidWithholdingException extends RuntimeException{
    public InvalidWithholdingException(){
        super("validation.document.withholding");
    }
}
