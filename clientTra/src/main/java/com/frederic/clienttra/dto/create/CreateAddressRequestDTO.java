package com.frederic.clienttra.dto.create;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
public class CreateAddressRequestDTO {//TODO validation message (see CreateUserRequestDTO)

    @NotBlank
    private String street;
    @NotBlank
    private String stNumber;
    private String apt;
    @NotBlank
    private String cp;
    @NotBlank
    private String city;
    private String state;
    @NotBlank
    private String country;
}

