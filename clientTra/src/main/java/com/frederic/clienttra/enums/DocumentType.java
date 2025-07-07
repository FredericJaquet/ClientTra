package com.frederic.clienttra.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentType {
    INV_CUST("INV_CUST"),
    INV_PROV("INV_PROV"),
    QUOTE("QUOTE"),
    PO("PO");

    private final String code;


    public static DocumentType fromCode(String code) {
        for (DocumentType type : values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de documento desconocido: " + code);
    }
}
