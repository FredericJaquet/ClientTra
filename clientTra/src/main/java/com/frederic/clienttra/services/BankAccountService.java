package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateBankAccountRequestDTO;
import com.frederic.clienttra.dto.read.BankAccountDTO;
import com.frederic.clienttra.dto.update.UpdateBankAccountRequestDTO;
import com.frederic.clienttra.entities.BankAccount;
import com.frederic.clienttra.entities.Company;
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

/**
 * Service class responsible for managing bank accounts associated with companies.
 * <p>
 * Provides methods to retrieve, create, update, and delete bank accounts with
 * appropriate ownership validation and error handling.
 */
@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final OwnerValidator ownerValidator;
    private final CompanyRepository companyRepository;
    private final DtoValidator dtoValidator;
    private final CompanyService companyService;

    /**
     * Retrieves all bank accounts for a specific company.
     *
     * @param idCompany the ID of the company
     * @return a list of {@link BankAccountDTO} for the company
     * @throws SecurityException if the current user is not authorized to access the company
     */
    @Transactional(readOnly = true)
    public List<BankAccountDTO> getAllBankAccounts(Integer idCompany){
        ownerValidator.checkOwner(idCompany);

        List<BankAccount> entities = bankAccountRepository.findByCompany_IdCompany(idCompany);

        return bankAccountMapper.toBankAccountDTOList(entities);
    }

    /**
     * Retrieves all bank accounts for the owner company.
     *
     * @return a list of {@link BankAccountDTO} for the company
     * @throws SecurityException if the current user is not authorized to access the company
     */
    @Transactional(readOnly = true)
    public List<BankAccountDTO> getAllBankAccounts(){
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<BankAccount> entities = bankAccountRepository.findByCompany_IdCompany(owner.getIdCompany());

        return bankAccountMapper.toBankAccountDTOList(entities);
    }

    /**
     * Retrieves a bank account by its ID and company ID.
     *
     * @param idCompany     the ID of the company owning the bank account
     * @param idBankAccount the ID of the bank account
     * @return the {@link BankAccountDTO} representing the requested bank account
     * @throws BankAccountNotFoundException if the bank account does not exist or does not belong to the company
     * @throws SecurityException             if the current user is not authorized to access the company
     */
    @Transactional(readOnly = true)
    public BankAccountDTO getBankAccount(Integer idCompany, Integer idBankAccount){
        ownerValidator.checkOwner(idCompany);

        BankAccount entity = bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany)
                .orElseThrow(BankAccountNotFoundException::new);
        return bankAccountMapper.toBankAccountDTO(entity);
    }

    /**
     * Retrieves a bank account entity by its ID and verifies ownership by the given company.
     *
     * @param idBankAccount the ID of the bank account
     * @param owner        the owning {@link Company}
     * @return the {@link BankAccount} entity
     * @throws BankAccountNotFoundException if the bank account does not exist or does not belong to the company
     */
    @Transactional
    public BankAccount getBankAccountByIdAndOwner(Integer idBankAccount, Company owner){
        return bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, owner.getIdCompany())
                .orElseThrow(BankAccountNotFoundException::new);
    }

    /**
     * Deletes a bank account associated with a company.
     *
     * @param idCompany     the ID of the company
     * @param idBankAccount the ID of the bank account to delete
     * @throws BankAccountNotFoundException if the bank account does not exist or does not belong to the company
     * @throws SecurityException             if the current user is not authorized to access the company
     */
    @Transactional
    public void deleteBankAccount(Integer idCompany, Integer idBankAccount){
        ownerValidator.checkOwner(idCompany);

        BankAccount entity = bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany)
                .orElseThrow(BankAccountNotFoundException::new);
        bankAccountRepository.delete(entity);
    }

    /**
     * Creates a new bank account for a company.
     *
     * @param idCompany the ID of the company
     * @param dto       the data transfer object containing bank account details
     * @throws CompanyNotFoundException if the company does not exist
     * @throws SecurityException        if the current user is not authorized to access the company
     */
    @Transactional
    public BankAccountDTO createBankAccount(Integer idCompany, CreateBankAccountRequestDTO dto){
        ownerValidator.checkOwner(idCompany);

        Company company = companyRepository.findByIdCompany(idCompany)
                .orElseThrow(CompanyNotFoundException::new);
        BankAccount entity = bankAccountMapper.toEntity(dto);
        entity.setCompany(company);
        BankAccount newAccount = bankAccountRepository.save(entity);

        return bankAccountMapper.toBankAccountDTO(newAccount);
    }

    /**
     * Updates an existing bank account of a company.
     * <p>
     * Validates the update DTO and applies changes.
     *
     * @param idCompany     the ID of the company
     * @param idBankAccount the ID of the bank account to update
     * @param dto           the update data transfer object
     * @return the updated {@link BankAccountDTO}
     * @throws BankAccountNotFoundException if the bank account does not exist or does not belong to the company
     * @throws SecurityException             if the current user is not authorized to access the company
     */
    @Transactional
    public BankAccountDTO updateBankAccount(Integer idCompany, Integer idBankAccount, UpdateBankAccountRequestDTO dto){
        ownerValidator.checkOwner(idCompany);
        BankAccount entity = bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany)
                .orElseThrow(BankAccountNotFoundException::new);
        CreateBankAccountRequestDTO createDto = bankAccountMapper.toCreateBankAccountRequestDTO(dto, entity);
        dtoValidator.validate(createDto);

        bankAccountMapper.updateEntity(entity, dto);
        BankAccount updated = bankAccountRepository.save(entity);

        return bankAccountMapper.toBankAccountDTO(updated);
    }

}
