package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateBankAccountRequestDTO;
import com.frederic.clienttra.dto.read.BankAccountDTO;
import com.frederic.clienttra.dto.update.UpdateBankAccountRequestDTO;
import com.frederic.clienttra.entities.BankAccount;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.AccessDeniedException;
import com.frederic.clienttra.exceptions.BankAccountNotFoundException;
import com.frederic.clienttra.exceptions.CompanyNotFoundException;
import com.frederic.clienttra.mappers.BankAccountMapper;
import com.frederic.clienttra.repositories.BankAccountRepository;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.validators.DtoValidator;
import com.frederic.clienttra.validators.OwnerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final OwnerValidator ownerValidator;
    private final CompanyRepository companyRepository;
    private final DtoValidator dtoValidator;

    @Transactional(readOnly = true)
    public List<BankAccountDTO> getAllBankAccounts(Integer idCompany){
        ownerValidator.checkOwner(idCompany);

        List<BankAccount> entities=bankAccountRepository.findByCompany_IdCompany(idCompany);

        return bankAccountMapper.toBankAccountDTOList(entities);
    }

    @Transactional(readOnly = true)
    public BankAccountDTO getBankAccount(Integer idCompany, Integer idBankAccount){
        ownerValidator.checkOwner(idCompany);

        BankAccount entity = bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany)
                .orElseThrow(BankAccountNotFoundException::new);
        return bankAccountMapper.toBankAccountDTO(entity);
    }

    @Transactional
    public BankAccount getBankAccountByIdAndOwner(Integer idBankAccount, Company owner){

        return bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, owner.getIdCompany())
                .orElseThrow(BankAccountNotFoundException::new);
    }

    @Transactional
    public void deleteBankAccount(Integer idCompany, Integer idBankAccount){
        ownerValidator.checkOwner(idCompany);

        BankAccount entity = bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany)
                .orElseThrow(BankAccountNotFoundException::new);
        bankAccountRepository.delete(entity);
    }

    @Transactional
    public void createBankAccount(Integer idCompany, CreateBankAccountRequestDTO dto){
        ownerValidator.checkOwner(idCompany);

        Company company=companyRepository.findByIdCompany(idCompany)
                .orElseThrow(CompanyNotFoundException::new);
        BankAccount entity = bankAccountMapper.toEntity(dto);
        entity.setCompany(company);
        bankAccountRepository.save(entity);
    }

    @Transactional
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
