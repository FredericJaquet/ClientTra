package com.frederic.clienttra.exceptions;

public class LogoNotLoadedException extends RuntimeException  {
    public LogoNotLoadedException() {
        super("error.logo_not_loaded");
    }
}
