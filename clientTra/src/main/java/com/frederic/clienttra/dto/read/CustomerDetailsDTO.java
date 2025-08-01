package com.frederic.clienttra.dto.read;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Detailed DTO for a customer.
 * Extends the base company DTO with additional customer-specific information such as
 * invoicing method, due date, payment method, default language, VAT rates, withholding rates,
 * geographic region flag, and enabled status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CustomerDetailsDTO extends BaseCompanyDTO {//TODO aquí faltan cosas... No me imprime los datos de este DTO...

    private Integer idCustomer;
    private String invoicingMethod;
    private Integer duedate;
    private String payMethod;
    private String defaultLanguage;
    private double defaultVat;
    private double defaultWithholding;
    private Boolean europe;
    private Boolean enabled;

}
