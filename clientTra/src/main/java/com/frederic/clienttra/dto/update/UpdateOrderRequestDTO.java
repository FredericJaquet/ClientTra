package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.create.CreateItemRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderRequestDTO {
    private Integer idorder;
    private String descrip;
    private LocalDate dateOrder;
    private Double pricePerUnit;
    private String units;
    private Double total=0.0;
    private String fieldName;
    private String sourceLanguage;
    private String targetLanguage;
    private List<CreateItemRequestDTO> lines;

}
