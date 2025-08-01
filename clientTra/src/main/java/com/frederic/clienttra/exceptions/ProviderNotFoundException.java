package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a provider is not found in the system.
 * The message "error.provider.not_found" can be used as a key for localized error messages.
 */
public class ProviderNotFoundException extends RuntimeException{
    public ProviderNotFoundException(){
        super("error.provider.not_found");
    }
}
