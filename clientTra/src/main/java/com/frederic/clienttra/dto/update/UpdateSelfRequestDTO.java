package com.frederic.clienttra.dto.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating the current user's own profile information.
 * Contains fields for email, preferred language, preferred theme,
 * and dark mode preference.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSelfRequestDTO {
    private String email;
    private String preferredLanguage;
    private String preferredTheme;
    private Boolean darkMode;
}
