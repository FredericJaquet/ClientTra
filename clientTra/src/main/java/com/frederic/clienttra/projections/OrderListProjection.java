package com.frederic.clienttra.projections;

import java.time.LocalDate;

public interface OrderListProjection {
    Integer getIdOrder();
    String getDescrip();
    LocalDate getDateOrder();
    Double getTotal();
    Boolean getBilled();
}
