package com.frederic.clienttra.dto.read;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * DTO for representing a company owner.
 * Extends the base company DTO to include additional details such as the company ID,
 * logo path, and associated users.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CompanyOwnerDTO extends BaseCompanyDTO {

    private String logoPath;
    private List<UserForAdminDTO> users;
}
