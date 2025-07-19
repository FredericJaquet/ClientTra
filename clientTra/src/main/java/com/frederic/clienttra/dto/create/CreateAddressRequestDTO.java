package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.dto.bases.BaseAddressDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAddressRequestDTO implements BaseAddressDTO {

    @NotBlank(message = "{validation.address.street_required}")
    private String street;
    @NotBlank(message = "{validation.address.street_number_required}")
    private String stNumber;
    private String apt;
    @NotBlank(message = "{validation.address.cp_required}")
    private String cp;
    @NotBlank(message = "{validation.address.city_required}")
    private String city;
    private String state;
    @NotBlank(message = "{validation.address.country_required}")
    private String country;
}

