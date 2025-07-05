package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.dto.bases.BaseItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO implements BaseItemDTO {
    private String descrip;
    private Double qty;
    private Double discount;
    private Double total;
}
