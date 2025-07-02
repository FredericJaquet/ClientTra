package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateAddressRequestDTO;
import com.frederic.clienttra.dto.create.CreateBankAccountRequestDTO;
import com.frederic.clienttra.dto.read.AddressDTO;
import com.frederic.clienttra.dto.read.BankAccountDTO;
import com.frederic.clienttra.dto.update.UpdateAddressRequestDTO;
import com.frederic.clienttra.dto.update.UpdateBankAccountRequestDTO;
import com.frederic.clienttra.entities.Address;
import com.frederic.clienttra.entities.BankAccount;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.AddressNotFoundException;
import com.frederic.clienttra.exceptions.BankAccountNotFoundException;
import com.frederic.clienttra.exceptions.CompanyNotFoundException;
import com.frederic.clienttra.mappers.BankAccountMapper;
import com.frederic.clienttra.repositories.BankAccountRepository;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.utils.validators.DtoValidator;
import com.frederic.clienttra.utils.validators.OwnerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final OwnerValidator ownerValidator;
    private final CompanyRepository companyRepository;
    private final DtoValidator dtoValidator;

    public List<BankAccountDTO> getAllBankAccounts(Integer idCompany){
        ownerValidator.checkOwner(idCompany);

        return bankAccountRepository.findByCompany_IdCompany(idCompany).stream()
                .map(p -> BankAccountDTO.builder()
                        .idBankAccount(p.getIdBankAccount())
                        .iban(p.getIban())
                        .swift(p.getSwift())
                        .branch(p.getBranch())
                        .holder(p.getHolder())
                        .build())
                .toList();
    }

    public BankAccountDTO getBankAccount(Integer idCompany, Integer idBankAccount){
        ownerValidator.checkOwner(idCompany);

        BankAccount entity = bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany)
                .orElseThrow(BankAccountNotFoundException::new);
        return bankAccountMapper.toBankAccountDTO(entity);
    }

    public void deleteBankAccount(Integer idCompany, Integer idBankAccount){
        ownerValidator.checkOwner(idCompany);

        BankAccount entity = bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany)
                .orElseThrow(BankAccountNotFoundException::new);
        bankAccountRepository.delete(entity);
    }

    public void createBankAccount(Integer idCompany, CreateBankAccountRequestDTO dto){
        ownerValidator.checkOwner(idCompany);

        Company company=companyRepository.findByIdCompany(idCompany)
                .orElseThrow(CompanyNotFoundException::new);
        BankAccount entity = bankAccountMapper.toEntity(dto);
        entity.setCompany(company);
        bankAccountRepository.save(entity);
    }

    public BankAccountDTO updateBankAccount(Integer idCompany, Integer idBankAccount, UpdateBankAccountRequestDTO dto){
        ownerValidator.checkOwner(idCompany);
        BankAccount entity = bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany)
                .orElseThrow(BankAccountNotFoundException::new);
        CreateBankAccountRequestDTO createDto = bankAccountMapper.toCreateBankAccountRequestDTO(dto,entity);
        dtoValidator.validate(createDto);

        bankAccountMapper.updateEntity(entity, dto);
        BankAccount updated = bankAccountRepository.save(entity);

        return bankAccountMapper.toBankAccountDTO(updated);
    }

}
