package com.frederic.clienttra.dto.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserForAdminDTO {
    private Integer idUser;
    private Integer idRole;
    private Boolean enabled;
}
