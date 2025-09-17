package com.frederic.clienttra.projections;

import java.time.LocalDate;

/**
 * Projection interface representing a summary view of orders.
 * Provides key order information for list displays.
 */
public interface OrderListForDashboardProjection {

    Integer getIdOrder();
    String getComName();
    String getDescrip();
    LocalDate getDateOrder();
    Double getTotal();
    Boolean getBilled();
}
