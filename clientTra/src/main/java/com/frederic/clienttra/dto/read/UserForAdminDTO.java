package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForAdminDTO {
    private Integer idUser;
    private String userName;
    private String email;
    private String roleName;
    private String planName;
}
