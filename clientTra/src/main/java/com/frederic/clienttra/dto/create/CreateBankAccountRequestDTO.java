package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.dto.bases.BaseBankAccountDTO;
import com.frederic.clienttra.validators.ValidIban;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a Bank Account.
 * <p>
 * Implements the BaseBankAccountDTO interface.
 * The IBAN field is validated using a custom @ValidIban annotation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBankAccountRequestDTO implements BaseBankAccountDTO {
    @ValidIban
    private String iban;
    private String swift;
    private String branch;
    private String holder;
}
