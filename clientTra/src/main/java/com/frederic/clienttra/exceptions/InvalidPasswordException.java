package com.frederic.clienttra.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("error.invalid_password");
    }
}
