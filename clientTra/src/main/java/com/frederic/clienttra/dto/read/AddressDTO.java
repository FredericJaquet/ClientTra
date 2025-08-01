package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.dto.bases.BaseAddressDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for reading address information.
 * Implements the base address interface to provide standardized access
 * to address-related fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO implements BaseAddressDTO {
    private Integer idAddress;
    private String street;
    private String stNumber;
    private String apt;
    private String cp;
    private String city;
    private String state;
    private String country;
}
