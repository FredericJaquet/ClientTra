package com.frederic.clienttra.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequestDTO {
    @NotBlank(message = "validation.username.required")
    private String username;
    @NotBlank(message = "validation.password.required")
    private String password;
    @Email(message = "validation.email.invalid")
    private String email;
    @NotNull(message = "validation.role_id.required")
    private Integer idRole;
    @NotNull(message = "validation.plan_id.required")
    private Integer idPlan;
}
