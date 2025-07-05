package com.frederic.clienttra.dto.update;

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
public class UpdateProviderRequestDTO extends UpdateBaseCompanyRequestDTO{
    private Integer duedate;
    private String defaultLanguage;
    private Double defaultVAT;
    private Double defaultWithholding;
    private Boolean europe;
    private Boolean enabled;
}
