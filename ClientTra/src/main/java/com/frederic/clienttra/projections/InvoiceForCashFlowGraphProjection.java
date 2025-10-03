package com.frederic.clienttra.projections;

import java.time.LocalDate;

/**
 * Projection interface representing invoice data for cash flow graph.
 * Provides key details including dates, and financial totals.
 */
public interface InvoiceForCashFlowGraphProjection {
    LocalDate getDocDate();
    Double getTotalNet();
}
