package com.frederic.clienttra.dto.update;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePasswordRequestDTO {
    @NotBlank
    private String currentPassword;
    @NotBlank
    private String newPassword;
}