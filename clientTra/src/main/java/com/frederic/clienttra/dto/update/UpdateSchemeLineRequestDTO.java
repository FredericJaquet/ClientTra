package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.bases.BaseSchemeLineDTO;
import com.frederic.clienttra.entities.Scheme;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSchemeLineRequestDTO implements BaseSchemeLineDTO {
    private Integer idSchemeLine;
    private String descrip;
    private Double discount;


}
