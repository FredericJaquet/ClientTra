package com.frederic.clienttra.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UpdateCustomerRequestDTO extends UpdateBaseCompanyRequestDTO{
    private String invoicingMethod;
    private Integer duedate;
    private String payMethod;
    private String defaultLanguage;
    private Double defaultVAT;
    private Double defaultWithholding;
    private Boolean europe;
    private Boolean enabled;
}
