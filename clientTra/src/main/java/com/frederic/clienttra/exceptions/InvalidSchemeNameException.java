package com.frederic.clienttra.exceptions;

public class InvalidSchemeNameException extends RuntimeException{
    public InvalidSchemeNameException(){
        super("validation.scheme.scheme_name_required");
    }
}
