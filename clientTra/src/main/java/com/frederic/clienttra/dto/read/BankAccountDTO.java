package com.frederic.clienttra.dto.read;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccountDTO {
    private Integer idBankAccount;
    private String iban;
    private String swift;
    private String branch;
    private String holder;
}
