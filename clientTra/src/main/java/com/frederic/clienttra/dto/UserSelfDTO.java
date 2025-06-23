package com.frederic.clienttra.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSelfDTO {
    private Integer idUser;
    private String userName;
    private String email;
    private String preferredLanguage;
    private String preferredTheme;
    private boolean darkMode;
    private String roleName;
    private String planName;
}
