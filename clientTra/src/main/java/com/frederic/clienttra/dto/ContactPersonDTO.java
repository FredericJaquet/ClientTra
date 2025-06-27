package com.frederic.clienttra.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactPersonDTO {
    private Integer idContactPerson;
    @NotBlank
    private String firstname;
    @NotBlank
    private String middlename;
    private String lastname;
    private String role;
    @Email
    private String email;

}
