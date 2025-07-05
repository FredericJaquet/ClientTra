package com.frederic.clienttra.dto.bases;

import java.time.LocalDate;
import java.util.List;

public interface BaseOrderDTO {
    String getDescrip();
    LocalDate getDateOrder();
    Double getPricePerUnit();
    String getUnits();
    Double getTotal();
    Boolean getBilled();
    String getFieldName();
    String getSourceLanguage();
    String getTargetLanguage();
    List<? extends BaseItemDTO> getItems();

}
