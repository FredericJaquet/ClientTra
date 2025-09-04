package com.frederic.clienttra.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO used to create a new user within the system.
 * Includes credentials, contact information, and role/plan associations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequestDTO {
    @NotBlank(message = "{validation.username.required}")
    private String username;
    @NotBlank(message = "{validation.password.required}")
    private String password;
    @Email(message = "{validation.email.invalid}")
    @Size(max = 100, message = "{validation.email.too_long}")
    private String email;
    private String preferredLanguage;
    private Integer idRole;
    private Integer idPlan;
}
