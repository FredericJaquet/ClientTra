package com.frederic.clienttra.dto.update;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * DTO for updating the owner's company information.
 * Extends base company update DTO with an additional logo path field.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UpdateCompanyOwnerRequestDTO extends UpdateBaseCompanyRequestDTO {
    private String logoPath;
}
