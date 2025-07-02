package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.dto.bases.BaseBankAccountDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
