package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing the current authenticated user’s own profile details.
 * Contains user ID, username, email, preferred language and theme settings, dark mode preference,
 * as well as role and plan information.
 */
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
