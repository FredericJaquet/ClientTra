package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for listing customers.
 * Contains basic customer information such as ID, commercial name,
 * VAT number, email, website, and enabled status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerForListDTO {
    private Integer idCustomer;
    private String comName;
    private String vatNumber;
    private String email;
    private String web;
    private Boolean enabled;

}
