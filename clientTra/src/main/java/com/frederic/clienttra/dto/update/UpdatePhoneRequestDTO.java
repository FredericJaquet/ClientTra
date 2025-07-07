package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.bases.BasePhoneDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePhoneRequestDTO implements BasePhoneDTO {
    //private Integer idPhone;
    private String phoneNumber;
    private String kind;
}