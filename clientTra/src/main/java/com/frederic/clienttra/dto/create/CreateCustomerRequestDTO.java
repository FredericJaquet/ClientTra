package com.frederic.clienttra.dto.create;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CreateCustomerRequestDTO extends CreateBaseCompanyRequestDTO {

    private String defaultLanguage="es";
    private Double defaultVAT=0.21;
    private Double defaultWithholding=0.15;
    private String invoicingMethod;
    private String payMethod;
    private Integer duedate;
    private Boolean europe=true;
}
