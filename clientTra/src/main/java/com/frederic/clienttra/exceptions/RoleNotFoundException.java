package com.frederic.clienttra.exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException() {
        super("error.role.not_found");
    }
}
