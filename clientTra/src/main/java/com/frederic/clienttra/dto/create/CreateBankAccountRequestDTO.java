package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.dto.bases.BaseBankAccountDTO;
import com.frederic.clienttra.utils.validators.ValidIban;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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