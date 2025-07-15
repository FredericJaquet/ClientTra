package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BaseBankAccountDTO;
import com.frederic.clienttra.dto.create.CreateBankAccountRequestDTO;
import com.frederic.clienttra.dto.read.BankAccountDTO;
import com.frederic.clienttra.dto.update.UpdateBankAccountRequestDTO;
import com.frederic.clienttra.entities.BankAccount;
import com.frederic.clienttra.entities.Company;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class BankAccountMapper {

    public List<BankAccountDTO> toBankAccountDTOList(List<BankAccount> entities){

        return entities.stream()
                .map(p -> BankAccountDTO.builder()
                        .idBankAccount(p.getIdBankAccount())
                        .iban(p.getIban())
                        .swift(p.getSwift())
                        .branch(p.getBranch())
                        .holder(p.getHolder())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
    }

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

    public List<BankAccount> toEntities(List< ? extends BaseBankAccountDTO> dtos){
        return dtos.stream().map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public BankAccount toEntity(BaseBankAccountDTO dto, Company company) {
        return BankAccount.builder()
                .iban(dto.getIban())
                .swift(dto.getSwift())
                .holder(dto.getHolder())
                .branch(dto.getBranch())
                .company(company)
                .build();
    }

    public List<BankAccount> toEntities(List< ? extends BaseBankAccountDTO> dtos, Company company){
        return dtos.stream()
                .map(dto -> toEntity(dto, company))
                .collect(Collectors.toCollection(ArrayList::new));
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
