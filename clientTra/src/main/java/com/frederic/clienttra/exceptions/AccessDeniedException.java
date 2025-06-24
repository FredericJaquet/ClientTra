package com.frederic.clienttra.exceptions;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("error.access_denied");
    }
}
