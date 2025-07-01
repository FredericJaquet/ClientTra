package com.frederic.clienttra.dto.update;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UpdateCompanyOwnerRequestDTO extends UpdateBaseCompanyRequestDTO {
    private String logoPath;
}
