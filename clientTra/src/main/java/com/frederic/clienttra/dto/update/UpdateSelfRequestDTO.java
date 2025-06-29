package com.frederic.clienttra.dto.update;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateSelfRequestDTO {
    private String email;
    private String preferredLanguage;
    private String preferredTheme;
    private Boolean darkMode;
}
