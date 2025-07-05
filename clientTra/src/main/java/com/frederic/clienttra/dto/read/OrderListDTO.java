package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderListDTO {
    private Integer idOrder;
    private String descrip;
    private LocalDate dateOrder;
    private Double total;
    private Boolean billed;
}
