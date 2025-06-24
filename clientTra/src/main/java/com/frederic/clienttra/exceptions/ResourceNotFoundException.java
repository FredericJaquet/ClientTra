package com.frederic.clienttra.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("error.role.not_found");
    }
}
