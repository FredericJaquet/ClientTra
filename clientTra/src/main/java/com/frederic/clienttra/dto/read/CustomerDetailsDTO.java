package com.frederic.clienttra.dto.read;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerDetailsDTO extends BaseCompanyDTO {

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
