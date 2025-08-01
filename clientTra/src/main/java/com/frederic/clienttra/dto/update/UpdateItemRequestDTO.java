package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.bases.BaseItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating item details.
 * Implements BaseItemDTO interface.
 * Includes fields for item ID, description, quantity, discount, and total amount.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateItemRequestDTO implements BaseItemDTO {
    private Integer idItem;
    private String descrip;
    private Double  qty;
    private Double discount;
    private Double total;

}
