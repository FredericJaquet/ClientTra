package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProviderDetailsDTO extends BaseCompanyDTO {
    private Integer idProvider;
    private String defaultLanguage;
    private Double defaultVAT;
    private Double defaultWithholding;
    private Integer duedate;
    private Boolean europe;
    private Boolean enable;
}
