package com.frederic.clienttra.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("error.user.not_found");
    }
}
