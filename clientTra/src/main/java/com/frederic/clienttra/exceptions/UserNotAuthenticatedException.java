package com.frederic.clienttra.exceptions;

public class UserNotAuthenticatedException extends RuntimeException{

    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}
