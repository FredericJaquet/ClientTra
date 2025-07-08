package com.frederic.clienttra.projections;

import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;

import java.time.LocalDate;

public interface DocumentListProjection {
    Integer getIdDocument();
    String getComName();
    String getDocNumber();
    LocalDate getDocDate();
    Double getTotalNet();
    String getCurrency();
    DocumentType getDocType();
    DocumentStatus getStatus();
}