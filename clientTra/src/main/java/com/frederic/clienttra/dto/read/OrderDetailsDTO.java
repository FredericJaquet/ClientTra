package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.dto.bases.BaseOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Detailed DTO for representing detailed information about an order.
 * Includes order identification, description, date, pricing details,
 * billing status, language information, associated company, and list of items.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailsDTO implements BaseOrderDTO {
    private Integer idOrder;
    private String descrip;
    private LocalDate dateOrder;
    private Double pricePerUnit;
    private String units;
    private Double total;
    private Boolean billed;
    private String fieldName;
    private String sourceLanguage;
    private String targetLanguage;
    private BaseCompanyMinimalDTO company;
    private List<ItemDTO> items;
}
