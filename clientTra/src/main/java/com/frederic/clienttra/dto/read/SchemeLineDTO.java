package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.dto.bases.BaseSchemeLineDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemeLineDTO implements BaseSchemeLineDTO {
    private String descrip;
    private Double discount;
}
