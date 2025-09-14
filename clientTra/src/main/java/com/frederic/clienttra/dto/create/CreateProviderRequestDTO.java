package com.frederic.clienttra.dto.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO used to create a new provider in the system.
 * Inherits general company data from CreateBaseCompanyRequestDTO and adds provider-specific defaults.
 * Includes fields for default language, VAT, withholding, due date, and EU status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CreateProviderRequestDTO extends CreateBaseCompanyRequestDTO {

    private String defaultLanguage="es";
    private Double defaultVat=0.21;
    private Double defaultWithholding=0.15;
    private Integer duedate;
    private Boolean europe=true;
}
