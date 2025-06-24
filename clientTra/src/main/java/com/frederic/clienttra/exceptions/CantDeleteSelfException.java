package com.frederic.clienttra.exceptions;

public class CantDeleteSelfException extends RuntimeException{
    public CantDeleteSelfException() {
        super("error.user.admin_cannot_delete_self");
    }
}
