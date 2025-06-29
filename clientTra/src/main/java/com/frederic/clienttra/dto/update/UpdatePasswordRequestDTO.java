package com.frederic.clienttra.dto.update;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePasswordRequestDTO {
    @NotBlank
    private String currentPassword;
    @NotBlank
    private String newPassword;
}