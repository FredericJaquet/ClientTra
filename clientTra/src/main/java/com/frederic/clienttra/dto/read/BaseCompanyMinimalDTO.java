package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Minimal DTO for company information.
 * Used to populate the two fields shown in an order when selecting a customer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseCompanyMinimalDTO {
    private Integer idCompany;
    private String comName;
    private String vatNumber;

}
