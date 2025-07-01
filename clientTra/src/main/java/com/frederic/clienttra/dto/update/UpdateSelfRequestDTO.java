package com.frederic.clienttra.dto.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
