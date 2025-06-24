package com.frederic.clienttra.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateDTO {
    @NotBlank(message = "validation.username.required")
    private String userName;

    @NotBlank(message = "validation.password.required")
    private String password;

    @Email(message = "validation.email.invalid")
    private String email;

}

