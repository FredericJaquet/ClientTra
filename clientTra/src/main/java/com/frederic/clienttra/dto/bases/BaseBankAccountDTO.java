package com.frederic.clienttra.dto.bases;

/**
 * Base interface for Bank Account DTOs.
 * Defines getter methods for common bank account fields.
 */
public interface BaseBankAccountDTO {
    String getIban();
    String getSwift();
    String getBranch();
    String getHolder();
}
