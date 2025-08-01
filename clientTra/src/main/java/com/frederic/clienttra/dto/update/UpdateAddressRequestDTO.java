package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.bases.BaseAddressDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating address information.
 * Implements the BaseAddressDTO interface.
 * Contains fields representing the components of an address.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAddressRequestDTO implements BaseAddressDTO {
    private String street;
    private String stNumber;
    private String apt;
    private String cp;
    private String city;
    private String state;
    private String country;
}
