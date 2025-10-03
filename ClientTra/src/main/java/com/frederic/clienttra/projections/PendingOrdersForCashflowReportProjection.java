package com.frederic.clienttra.projections;

import java.time.LocalDate;

public interface PendingOrdersForCashflowReportProjection {
    Integer getIdOrder();
    Double getTotal();
    String getLegalName();
    Integer getIdCompany();
    LocalDate getDateOrder();
}
