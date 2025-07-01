package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactPersonDTO {
    private Integer idContactPerson;
    private String firstname;
    private String middlename;
    private String lastname;
    private String role;
    private String email;

}
