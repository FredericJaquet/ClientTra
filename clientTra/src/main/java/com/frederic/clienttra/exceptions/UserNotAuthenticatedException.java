package com.frederic.clienttra.exceptions;

public class UserNotAuthenticatedException extends RuntimeException{

    public UserNotAuthenticatedException()
    {
        super("error.not_authenticated");
    }
}
