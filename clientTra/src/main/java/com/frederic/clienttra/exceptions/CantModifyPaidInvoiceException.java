package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when trying to modify an invoice that is paid or not in pending status.
 * Uses the message key "error.invoice.cannot_modify_if_not_pending" for localization.
 */
public class CantModifyPaidInvoiceException extends RuntimeException{
    public CantModifyPaidInvoiceException(){
        super("error.invoice.cannot_modify_if_not_pending");
    }
}
