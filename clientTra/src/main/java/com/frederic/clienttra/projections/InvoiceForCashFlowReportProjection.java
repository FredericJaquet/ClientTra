package com.frederic.clienttra.projections;

import java.time.LocalDate;

/**
 * Projection interface representing invoice data for cash flow reports.
 * Provides key invoice details including company info, dates, and financial totals.
 */
public interface InvoiceForCashFlowReportProjection {
    Integer getIdCompany();
    String getLegalName();
    String getVatNumber();
    String getInvoiceNumber();
    LocalDate getDocDate();
    Double getTotalNet();
    Double getTotalVat();
    Double getTotalWithholding();

}
