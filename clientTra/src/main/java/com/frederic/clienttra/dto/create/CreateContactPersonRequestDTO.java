package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.dto.bases.BaseContactPersonDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a contact person associated with a company or entity.
 * <p>
 * Includes the person's first name, middle name, optional last name,
 * their role, and an email address.
 * <p>
 * Validations ensure first and middle names are not blank, and
 * email is well-formed and does not exceed 100 characters.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateContactPersonRequestDTO implements BaseContactPersonDTO {
    @NotBlank(message = "{validation.contact.firstname_required}")
    private String firstname;
    @NotBlank(message = "{validation.contact.middlename_required}")
    private String middlename;
    private String lastname;
    private String role;
    @Email(message = "{validation.email.invalid}")
    @Size(max = 100, message = "{validation.email.too_long}")
    private String email;
}
