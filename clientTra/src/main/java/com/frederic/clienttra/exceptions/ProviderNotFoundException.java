package com.frederic.clienttra.exceptions;

public class ProviderNotFoundException extends RuntimeException{
    public ProviderNotFoundException(){
        super("error.provider.not_found");
    }
}
