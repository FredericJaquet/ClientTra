package com.frederic.clienttra.dto.create;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * DTO for creating a customer entity, extending the base company creation DTO.
 * <p>
 * Adds customer-specific default settings such as language, VAT rate, withholding,
 * invoicing and payment methods, due date, and EU status.
 * <p>
 * Default values are provided for language ("es"), VAT (21%), withholding (15%),
 * and Europe flag (true).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CreateCustomerRequestDTO extends CreateBaseCompanyRequestDTO {

    private String defaultLanguage = "es";
    private Double defaultVAT = 0.21;
    private Double defaultWithholding = 0.15;
    private String invoicingMethod;
    private String payMethod;
    private Integer duedate;
    private Boolean europe = true;
}

