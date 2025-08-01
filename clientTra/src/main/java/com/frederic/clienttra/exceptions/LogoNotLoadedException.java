package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a logo file fails to load.
 * The message key "error.logo_not_loaded" can be used for localized error messages.
 */
public class LogoNotLoadedException extends RuntimeException  {
    public LogoNotLoadedException() {
        super("error.logo_not_loaded");
    }
}
