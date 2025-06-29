package com.frederic.clienttra.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateContactPersonRequestDTO {//TODO validation message (see CreateUserRequestDTO)
    private Integer idContactPerson;
    @NotBlank
    private String firstname;
    @NotBlank
    private String middlename;
    private String lastname;
    private String role;
    @Email(message = "validation.email.invalid")
    @Size(max = 100, message = "validation.email.too_long")
    private String email;

}