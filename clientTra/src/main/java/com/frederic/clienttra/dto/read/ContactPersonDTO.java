package com.frederic.clienttra.dto.read;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactPersonDTO {
    private Integer idContactPerson;
    private String firstname;
    private String middlename;
    private String lastname;
    private String role;
    private String email;

}
