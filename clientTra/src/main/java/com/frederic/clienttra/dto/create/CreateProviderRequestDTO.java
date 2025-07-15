package com.frederic.clienttra.dto.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CreateProviderRequestDTO extends CreateBaseCompanyRequestDTO {

    private String defaultLanguage="es";
    private Double defaultVAT=0.21;
    private Double defaultWithholding=0.15;
    private Integer duedate;
    private Boolean europe=true;
}
