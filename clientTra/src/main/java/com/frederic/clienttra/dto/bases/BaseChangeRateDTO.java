package com.frederic.clienttra.dto.bases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public interface BaseChangeRateDTO {

    String getCurrency1();
    String getCurrency2();
    Double getRate();
    LocalDate getDate();


}
