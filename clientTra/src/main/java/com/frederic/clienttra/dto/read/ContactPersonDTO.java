package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.dto.bases.BaseContactPersonDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing a contact person.
 * Implements the base contact person DTO interface.
 * Contains identification and contact details including first name, middle name,
 * last name, role, and email address.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactPersonDTO implements BaseContactPersonDTO {
    private Integer idContactPerson;
    private String firstname;
    private String middlename;
    private String lastname;
    private String role;
    private String email;

}
