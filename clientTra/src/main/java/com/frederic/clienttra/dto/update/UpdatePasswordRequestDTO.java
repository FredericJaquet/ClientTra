package com.frederic.clienttra.dto.update;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating a user's password.
 * Contains the current password and the new password fields, both required (not blank).
 * Note: The frontend should verify that the new password is entered twice and both entries match before sending the request.
 */
@Data
@NoArgsConstructor
public class UpdatePasswordRequestDTO {
    @NotBlank
    private String currentPassword;
    @NotBlank
    private String newPassword;
}