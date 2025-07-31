package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.dto.bases.BaseItemDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO used to represent a line item within an order.
 * It contains essential data such as quantity, description, discounts and calculated totals.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateItemRequestDTO implements BaseItemDTO {
    @NotBlank(message = "validation.item.description_required")
    private String descrip;
    @NotNull(message = "validation.item.quantity_required")
    private Double qty;
    private Double discount = 0.0;
    private Double total = 0.0;
}
