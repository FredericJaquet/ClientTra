package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.dto.bases.BaseSchemeLineDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO used to create a new line within a pricing scheme.
 * Implements the BaseSchemeLineDTO interface for consistency across scheme line data structures.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSchemeLineRequestDTO implements BaseSchemeLineDTO {
    private String descrip;
    private Double discount;
}
