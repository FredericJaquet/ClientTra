package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.bases.BaseSchemeLineDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating a scheme line.
 * Implements the BaseSchemeLineDTO interface.
 * Represents the description and discount of a scheme line.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSchemeLineRequestDTO implements BaseSchemeLineDTO {
    private Integer idSchemeLine;
    private String descrip;
    private Double discount;

}
