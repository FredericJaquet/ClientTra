package com.frederic.clienttra.dto.read;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchemeLineDTO {
    private String descrip;
    private Double discount;
}
