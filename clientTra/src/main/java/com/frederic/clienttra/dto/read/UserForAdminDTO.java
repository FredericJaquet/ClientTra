package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing a user with details relevant for administrative purposes.
 * Includes user's ID, username, email, role, and assigned plan.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForAdminDTO {
    private Integer idUser;
    private String userName;
    private String email;
    private String roleName;
    private String planName;
}
