package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.dto.bases.BaseBankAccountDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for reading bank account information.
 * Implements the base bank account interface to provide standardized access
 * to bank account-related fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountDTO implements BaseBankAccountDTO {
    private Integer idBankAccount;
    private String iban;
    private String swift;
    private String branch;
    private String holder;
}
