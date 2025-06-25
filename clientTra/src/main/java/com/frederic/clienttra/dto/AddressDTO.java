package com.frederic.clienttra.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDTO {
    private Integer idAddress;
    private String street;
    private String stNumber;
    private String apt;
    private String cp;
    private String city;
    private String state;
    private String country;
}
