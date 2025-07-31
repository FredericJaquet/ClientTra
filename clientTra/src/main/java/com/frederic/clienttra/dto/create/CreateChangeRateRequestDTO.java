package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.dto.bases.BaseChangeRateDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO for creating a currency exchange rate entry.
 * <p>
 * Includes the source and target currencies, the exchange rate value,
 * and the effective date of the rate.
 * <p>
 * Validation ensures currency codes are not blank, and rate and date are not null.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateChangeRateRequestDTO implements BaseChangeRateDTO {
    @NotBlank(message="validation.change_rate.currency1_required")
    private String currency1;
    @NotBlank(message="validation.change_rate.currency2_required")
    private String currency2;
    @NotNull(message="validation.change_rate.rate_Required")
    private Double rate;
    @NotNull(message="validation.change_rate.date_Required")
    private LocalDate date;
}
