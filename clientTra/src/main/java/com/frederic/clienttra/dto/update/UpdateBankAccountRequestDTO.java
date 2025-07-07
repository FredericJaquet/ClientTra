package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.bases.BaseBankAccountDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBankAccountRequestDTO implements BaseBankAccountDTO {
    private String iban;
    private String swift;
    private String branch;
    private String holder;
}