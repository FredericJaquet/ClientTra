package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.bases.BaseOrderDTO;
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
