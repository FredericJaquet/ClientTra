package com.frederic.clienttra.projections;

import com.frederic.clienttra.enums.DocumentStatus;

import java.time.LocalDate;

/**
 * Projection interface representing invoice data for pending reports.
 * Provides essential invoice information including company, amounts, status, and dates.
 */
public interface InvoiceForPendingReportProjection {
    Integer getIdDocument();
    String getComName();
    String getDocNumber();
    Double getTotalToPay();
    DocumentStatus getStatus();
    LocalDate getDocDate();

}
