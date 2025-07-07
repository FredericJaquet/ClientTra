package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.bases.BaseContactPersonDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateContactPersonRequestDTO implements BaseContactPersonDTO {
    //private Integer idContactPerson;
    private String firstname;
    private String middlename;
    private String lastname;
    private String role;
    private String email;

}