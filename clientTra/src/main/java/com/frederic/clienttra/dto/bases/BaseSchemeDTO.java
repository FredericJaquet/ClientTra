package com.frederic.clienttra.dto.bases;

import com.frederic.clienttra.dto.read.SchemeLineDTO;

import java.util.List;

public interface BaseSchemeDTO {
    String getSchemeName();
    Double getPrice();
    String getUnits();
    String getFieldName();
    String getSourceLanguage();
    String getTargetLanguage();
    List<? extends BaseSchemeLineDTO> getSchemeLines();
}
