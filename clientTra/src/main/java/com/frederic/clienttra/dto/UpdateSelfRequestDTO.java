package com.frederic.clienttra.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class UpdateSelfRequestDTO {
    @Email(message = "validation.email.invalid")
    @Size(max = 100, message = "validation.email.too_long")
    private String email;
    private String preferredLanguage;
    private String preferredTheme;
    private Boolean darkMode;
}
