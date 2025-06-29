package com.frederic.clienttra.dto.update;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateBankAccountRequestDTO {
    private Integer idBankAccount;
    private String iban;
    private String swift;
    private String branch;
    private String holder;
}