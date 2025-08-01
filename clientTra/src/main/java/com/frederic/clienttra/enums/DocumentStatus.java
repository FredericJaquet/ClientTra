package com.frederic.clienttra.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing the possible statuses of a document.
 * Includes a utility method to retrieve an enum value from a string code.
 */
@Getter
@RequiredArgsConstructor
public enum DocumentStatus {
    PENDING("PENDING"),
    PAID("PAID"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED"),
    MODIFIED("MODIFIED"),
    DELETED("DELETED");

    private final String code;

    /**
     * Converts a string code to a DocumentStatus enum, ignoring case.
     *
     * @param code the string representation of the status
     * @return the matching DocumentStatus enum
     * @throws IllegalArgumentException if the code doesn't match any status
     */
    public static DocumentStatus fromCode(String code) {
        for (DocumentStatus status : values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown document status: " + code);
    }
}
