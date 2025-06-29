package com.frederic.clienttra.dto.create;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class CreateCustomerRequestDTO extends CreateBaseCompanyRequestDTO {//TODO validation message (see CreateUserRequestDTO)

    private String defaultLanguage="es";
    private Double defaultVAT=0.21;
    private Double defaultWithholding=0.15;
    private String invoicingMethod;
    private String payMethod;
    private Integer duedate;
    private Boolean europe=true;
}
