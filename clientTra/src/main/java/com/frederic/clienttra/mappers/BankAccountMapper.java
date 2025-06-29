package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.create.CreateBankAccountRequestDTO;
import com.frederic.clienttra.dto.read.BankAccountDTO;
import com.frederic.clienttra.dto.update.UpdateBankAccountRequestDTO;
import com.frederic.clienttra.entities.BankAccount;
import org.springframework.stereotype.Component;


@Component
public class BankAccountMapper {

    public BankAccountDTO toBankAccountDTO(BankAccount bankAccount){
        return BankAccountDTO.builder()
                .idBankAccount(bankAccount.getIdBankAccount())
                .iban(bankAccount.getIban())
                .swift(bankAccount.getSwift())
                .holder(bankAccount.getHolder())
                .branch((bankAccount.getBranch()))
                .build();
    }

    public BankAccount toEntity(CreateBankAccountRequestDTO dto) {
        return BankAccount.builder()
                .idBankAccount(dto.getIdBankAccount())
                .iban(dto.getIban())
                .swift(dto.getSwift())
                .holder(dto.getHolder())
                .branch(dto.getBranch())
                .build();
    }

    public BankAccount toEntity(UpdateBankAccountRequestDTO dto) {
        return BankAccount.builder()
                .idBankAccount(dto.getIdBankAccount())
                .iban(dto.getIban())
                .swift(dto.getSwift())
                .holder(dto.getHolder())
                .branch(dto.getBranch())
                .build();
    }

    public void updateEntity(BankAccount entity, UpdateBankAccountRequestDTO dto) {
        entity.setIban(dto.getIban());
        entity.setSwift(dto.getSwift());
        entity.setHolder(dto.getHolder());
        entity.setBranch(dto.getBranch());
    }
}
