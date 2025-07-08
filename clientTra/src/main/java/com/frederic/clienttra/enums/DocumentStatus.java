package com.frederic.clienttra.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentStatus {
    PENDING("PENDING"),
    PAID("PAID"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED"),
    MODIFIED("MODIFIED");

    private final String code;

    public static DocumentStatus fromCode(String code) {
        for (DocumentStatus status : values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado de documento desconocido: " + code);
    }
}
