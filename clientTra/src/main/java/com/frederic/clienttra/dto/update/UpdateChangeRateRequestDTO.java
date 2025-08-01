package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.bases.BaseChangeRateDTO;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO for updating change rate information.
 * Includes currency pair, rate value, and the date of the rate.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateChangeRateRequestDTO implements BaseChangeRateDTO {
    private String currency1;
    private String currency2;
    private Double rate;
    private LocalDate date;
}
