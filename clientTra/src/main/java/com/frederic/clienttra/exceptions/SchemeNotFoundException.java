package com.frederic.clienttra.exceptions;

public class SchemeNotFoundException extends RuntimeException {
    public SchemeNotFoundException() {
        super("error.scheme.not_found");
    }
}
