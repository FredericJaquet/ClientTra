package com.frederic.clienttra.dto;

import com.frederic.clienttra.utils.validators.ValidIban;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccountDTO {
    private Integer idBankAccount;
    @ValidIban
    private String iban;
    private String swift;
    private String branch;
    private String holder;
}
