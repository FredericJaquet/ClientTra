package com.frederic.clienttra.dto.update;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePhoneRequestDTO {
    private Integer idPhone;
    private String phoneNumber;
    private String kind;
}