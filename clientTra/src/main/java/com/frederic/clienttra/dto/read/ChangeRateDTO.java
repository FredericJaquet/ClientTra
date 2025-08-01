package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.dto.bases.BaseChangeRateDTO;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO for representing a currency exchange rate.
 * Implements the base interface for change rate data.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeRateDTO implements BaseChangeRateDTO {
    private Integer idChangeRate;
    private String currency1;
    private String currency2;
    private Double rate;
    private LocalDate date;

}
