package com.frederic.clienttra.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserForAdminDTO {
    private Integer idUser;
    private String userName;
    private String email;
    private String roleName;
    private String planName;
}
