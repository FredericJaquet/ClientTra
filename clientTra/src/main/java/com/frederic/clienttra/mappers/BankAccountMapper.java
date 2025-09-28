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

/**
 * Mapper class responsible for converting between BankAccount entities and various BankAccount DTOs.
 * <p>
 * Provides methods to map entities to DTOs, DTOs to entities, update entities from DTOs,
 * and handle lists of these objects.
 * </p>
 */
@Component
public class BankAccountMapper {

    /**
     * Converts a list of BankAccount entities into a list of BankAccountDTOs.
     *
     * @param entities the list of BankAccount entities to convert
     * @return a list of BankAccountDTO objects representing the entities
     */
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

    /**
     * Converts a single BankAccount entity into a BankAccountDTO.
     *
     * @param bankAccount the BankAccount entity to convert
     * @return a BankAccountDTO representing the given entity
     */
    public BankAccountDTO toBankAccountDTO(BankAccount bankAccount){
        if(bankAccount == null){
            return null;
        }
        return BankAccountDTO.builder()
                .idBankAccount(bankAccount.getIdBankAccount())
                .iban(bankAccount.getIban())
                .swift(bankAccount.getSwift())
                .holder(bankAccount.getHolder())
                .branch(bankAccount.getBranch())
                .build();
    }

    /**
     * Converts an UpdateBankAccountRequestDTO and an existing BankAccount entity
     * into a CreateBankAccountRequestDTO, merging updated fields and existing values.
     *
     * @param dto the UpdateBankAccountRequestDTO containing new values (nullable)
     * @param entity the existing BankAccount entity with current values
     * @return a CreateBankAccountRequestDTO with updated and existing values combined
     */
    public CreateBankAccountRequestDTO toCreateBankAccountRequestDTO(UpdateBankAccountRequestDTO dto, BankAccount entity){
        return CreateBankAccountRequestDTO.builder()
                .iban(dto.getIban() != null ? dto.getIban() : entity.getIban())
                .swift(dto.getSwift() != null ? dto.getSwift() : entity.getSwift())
                .branch(dto.getBranch() != null ? dto.getBranch() : entity.getBranch())
                .holder(dto.getHolder() != null ? dto.getHolder() : entity.getHolder())
                .build();
    }

    /**
     * Converts a BaseBankAccountDTO into a BankAccount entity.
     *
     * @param dto the BaseBankAccountDTO to convert
     * @return a BankAccount entity built from the DTO data
     */
    public BankAccount toEntity(BaseBankAccountDTO dto) {
        return BankAccount.builder()
                .iban(dto.getIban())
                .swift(dto.getSwift())
                .holder(dto.getHolder())
                .branch(dto.getBranch())
                .build();
    }

    /**
     * Converts a list of BaseBankAccountDTO (or subclasses) into a list of BankAccount entities.
     *
     * @param dtos the list of BaseBankAccountDTO objects to convert
     * @return a list of BankAccount entities
     */
    public List<BankAccount> toEntities(List< ? extends BaseBankAccountDTO> dtos){
        return dtos.stream().map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a BaseBankAccountDTO and associates it with a Company, returning a BankAccount entity.
     *
     * @param dto the BaseBankAccountDTO to convert
     * @param company the Company entity to associate with the BankAccount
     * @return a BankAccount entity built from the DTO and linked to the given Company
     */
    public BankAccount toEntity(BaseBankAccountDTO dto, Company company) {
        return BankAccount.builder()
                .iban(dto.getIban())
                .swift(dto.getSwift())
                .holder(dto.getHolder())
                .branch(dto.getBranch())
                .company(company)
                .build();
    }

    /**
     * Converts a list of BaseBankAccountDTO (or subclasses), associating each BankAccount entity with the given Company.
     *
     * @param dtos the list of BaseBankAccountDTO objects to convert
     * @param company the Company entity to associate with each BankAccount
     * @return a list of BankAccount entities linked to the specified Company
     */
    public List<BankAccount> toEntities(List< ? extends BaseBankAccountDTO> dtos, Company company){
        return dtos.stream()
                .map(dto -> toEntity(dto, company))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Updates an existing BankAccount entity with non-null values from an UpdateBankAccountRequestDTO.
     *
     * @param entity the BankAccount entity to update
     * @param dto the UpdateBankAccountRequestDTO containing new values (nullable)
     */
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
