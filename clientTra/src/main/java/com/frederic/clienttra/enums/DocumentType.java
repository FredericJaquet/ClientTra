package com.frederic.clienttra.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the types of documents in the system.
 * Includes customer and provider invoices, quotes, and purchase orders.
 */
@Getter
@RequiredArgsConstructor
public enum DocumentType {
    INV_CUST("INV_CUST"), // Customer invoice
    INV_PROV("INV_PROV"), // Provider invoice
    QUOTE("QUOTE"),       // Quote or budget
    PO("PO");             // Purchase order

    private final String code;

    /**
     * Converts a string code to a DocumentType enum, ignoring case.
     *
     * @param code the string representation of the document type
     * @return the matching DocumentType enum
     * @throws IllegalArgumentException if the code doesn't match any type
     */
    public static DocumentType fromCode(String code) {
        for (DocumentType type : values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown document type: " + code);
    }
}
