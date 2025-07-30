package com.frederic.clienttra.projections;

import java.time.LocalDate;

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
