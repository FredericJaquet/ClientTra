package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for representing a summarized view of an order.
 * Contains order ID, description, date, total amount, and billing status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderListDTO {
    private Integer idOrder;
    private String comName;
    private String descrip;
    private LocalDate dateOrder;
    private Double total;
    private Boolean billed;
}
