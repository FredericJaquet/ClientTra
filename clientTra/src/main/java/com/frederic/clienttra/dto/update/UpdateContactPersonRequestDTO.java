package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.bases.BaseContactPersonDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating contact person information.
 * Implements the BaseContactPersonDTO interface.
 *
 * The ID field is commented out, assuming updates are done without changing the ID.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateContactPersonRequestDTO implements BaseContactPersonDTO {
    private String firstname;
    private String middlename;
    private String lastname;
    private String role;
    private String email;

}