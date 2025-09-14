package com.frederic.clienttra.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for updating a provider's information.
 * Extends the UpdateBaseCompanyRequestDTO.
 * Contains additional fields related to provider-specific details such as
 * payment due date, default language, VAT and withholding rates,
 * whether the provider is located in Europe, and if the provider is enabled.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UpdateProviderRequestDTO extends UpdateBaseCompanyRequestDTO{
    private Integer duedate;
    private String defaultLanguage;
    private Double defaultVat;
    private Double defaultWithholding;
    private Boolean europe;
    private Boolean enabled;
}
