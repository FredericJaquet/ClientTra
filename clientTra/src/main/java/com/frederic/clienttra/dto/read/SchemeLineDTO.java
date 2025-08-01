package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.dto.bases.BaseSchemeLineDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing a line item within a billing or pricing scheme.
 * Contains description and discount details for the scheme line.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemeLineDTO implements BaseSchemeLineDTO {
    private String descrip;
    private Double discount;
}
