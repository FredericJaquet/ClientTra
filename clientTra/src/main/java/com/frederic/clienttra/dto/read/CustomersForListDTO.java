package com.frederic.clienttra.dto.read;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomersForListDTO {

    private Integer idCustomer;
    private String comName;
    private String vatNumber;
    private String email;
    private String web;
    private Boolean enabled;
}
