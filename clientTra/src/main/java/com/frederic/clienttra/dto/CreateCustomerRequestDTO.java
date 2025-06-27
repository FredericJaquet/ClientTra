package com.frederic.clienttra.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class CreateCustomerRequestDTO extends BaseCompanyRequestDTO {

    private String defaultLanguage="es";
    private Double defaultVAT=0.21;
    private Double defaultWithholding=0.15;
    private String invoicingMethod;
    private String payMethod;
    private Integer duedate;
    private Boolean europe=true;

}
