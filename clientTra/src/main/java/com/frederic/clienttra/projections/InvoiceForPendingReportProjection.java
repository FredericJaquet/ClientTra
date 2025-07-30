package com.frederic.clienttra.projections;

import com.frederic.clienttra.enums.DocumentStatus;

import java.time.LocalDate;

public interface InvoiceForPendingReportProjection {
    Integer getIdDocument();
    String getComName();
    String getDocNumber();
    Double getTotalToPay();
    DocumentStatus getStatus();
    LocalDate getDocDate();

}
