package com.frederic.clienttra.projections;

import java.time.LocalDate;

public interface PendingOrdersForCashflowReportProjection {
    Integer getIdOrder();
    Double getTotal();
    Integer getIdCompany();
    LocalDate getDateOrder();
}
