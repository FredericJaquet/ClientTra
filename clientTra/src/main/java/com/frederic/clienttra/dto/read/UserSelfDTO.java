package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
