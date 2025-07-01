package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BaseBankAccountDTO;
import com.frederic.clienttra.dto.create.CreateBankAccountRequestDTO;
import com.frederic.clienttra.dto.create.CreatePhoneRequestDTO;
import com.frederic.clienttra.dto.read.BankAccountDTO;
import com.frederic.clienttra.dto.update.UpdateBankAccountRequestDTO;
import com.frederic.clienttra.dto.update.UpdatePhoneRequestDTO;
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

    public CreateBankAccountRequestDTO toCreateBankAccountRequestDTO(UpdateBankAccountRequestDTO dto, BankAccount entity){
        return CreateBankAccountRequestDTO.builder()
                .iban(dto.getIban() != null ? dto.getIban() : entity.getIban())
                .swift(dto.getSwift() != null ? dto.getSwift() : entity.getSwift())
                .branch(dto.getBranch() != null ? dto.getBranch() : entity.getBranch())
                .holder(dto.getHolder() != null ? dto.getHolder() : entity.getHolder())
                .build();
    }

    public BankAccount toEntity(BaseBankAccountDTO dto) {
        return BankAccount.builder()
                .iban(dto.getIban())
                .swift(dto.getSwift())
                .holder(dto.getHolder())
                .branch(dto.getBranch())
                .build();
    }

    public void updateEntity(BankAccount entity, UpdateBankAccountRequestDTO dto) {
        if(dto.getIban() != null){
            entity.setIban(dto.getIban());
        }
        if(dto.getSwift() != null){
            entity.setSwift(dto.getSwift());
        }
        if(dto.getHolder() != null){
            entity.setHolder(dto.getHolder());
        }
        if(dto.getBranch() != null){
            entity.setBranch(dto.getBranch());
        }
    }
}
