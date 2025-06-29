package com.frederic.clienttra.dto.create;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePhoneRequestDTO {//TODO validation message (see CreateUserRequestDTO)
    private Integer idPhone;
    @NotBlank
    private String phoneNumber;
    private String kind;
}