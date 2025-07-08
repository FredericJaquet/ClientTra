package com.frederic.clienttra.exceptions;

public class CantModifyPaidInvoiceException extends RuntimeException{
    public CantModifyPaidInvoiceException(){
        super("error.invoice.cannot_modify_if_not_pending");
    }
}
