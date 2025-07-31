package com.frederic.clienttra.dto.bases;

import java.time.LocalDate;

/**
 * Base interface for Change Rate DTOs.
 * Defines getter methods for currency exchange rate details.
 */
public interface BaseChangeRateDTO {
    String getCurrency1();
    String getCurrency2();
    Double getRate();
    LocalDate getDate();

}
