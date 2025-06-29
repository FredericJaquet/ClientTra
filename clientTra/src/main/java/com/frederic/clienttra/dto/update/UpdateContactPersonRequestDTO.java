package com.frederic.clienttra.dto.update;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateContactPersonRequestDTO {
    private Integer idContactPerson;
    private String firstname;
    private String middlename;
    private String lastname;
    private String role;
    private String email;

}