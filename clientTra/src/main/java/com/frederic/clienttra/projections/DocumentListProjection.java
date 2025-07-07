package com.frederic.clienttra.projections;

import java.time.LocalDate;

public interface DocumentListProjection {
    Integer getIdDocument();
    String getComName();
    String getDocNumber();
    LocalDate getDocDate();
    Double getTotalNet();
    String getCurrency();
    String getDocType();
}