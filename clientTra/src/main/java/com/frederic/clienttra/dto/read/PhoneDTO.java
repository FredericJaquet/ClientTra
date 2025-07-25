package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.dto.bases.BasePhoneDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneDTO implements BasePhoneDTO {
    private Integer idPhone;
    private String phoneNumber;
    private String kind;
}
