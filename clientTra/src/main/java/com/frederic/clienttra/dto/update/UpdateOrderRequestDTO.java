package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.bases.BaseOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for updating order details.
 * Implements BaseOrderDTO interface.
 * Contains fields for description, order date, price per unit, units, total amount,
 * billed status, field name, source and target languages, and a list of items.
 * Includes a helper method to add an item to the order's item list (used in tests).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderRequestDTO implements BaseOrderDTO {
    private String descrip;
    private LocalDate dateOrder;
    private Double pricePerUnit;
    private String units;
    private Double total;
    private Boolean billed;
    private String fieldName;
    private String sourceLanguage;
    private String targetLanguage;
    private List<UpdateItemRequestDTO> items;

    public void addItem(UpdateItemRequestDTO item){//Used in Test
        items.add(item);
    }

}
