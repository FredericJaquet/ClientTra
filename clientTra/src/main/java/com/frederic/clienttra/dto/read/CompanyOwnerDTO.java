package com.frederic.clienttra.dto.read;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CompanyOwnerDTO extends BaseCompanyDTO {
    private int idCompany;
    private String logoPath;
    private List<UserForAdminDTO> users;
}
