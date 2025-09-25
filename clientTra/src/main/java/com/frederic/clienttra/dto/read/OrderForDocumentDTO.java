package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for representing a summarized view of an order for documents.
 * Contains order ID, description, date, quantity, total amount and list of items.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderForDocumentDTO {
    private Integer idOrder;
    private String descrip;
    private LocalDate dateOrder;
    private Double pricePerUnit;
    private String units;
    private Double quantity;
    private Double total;
    private Boolean billed;
    private List<ItemDTO> items;

}
