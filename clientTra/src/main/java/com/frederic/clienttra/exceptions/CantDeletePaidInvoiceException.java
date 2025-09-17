package com.frederic.clienttra.exceptions;

public class CantDeletePaidInvoiceException extends RuntimeException{
    public CantDeletePaidInvoiceException(){
        super("error.invoice.cannot_delete_order_if_not_pending");
    }
}
