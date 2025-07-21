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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BankAccountServiceTest {

    @Mock
    private BankAccountMapper bankAccountMapper;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private DtoValidator dtoValidator;
    @Mock
    private OwnerValidator ownerValidator;
    @InjectMocks
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBankAccounts_shouldReturnBankAccountDTO(){
        int idCompany=1;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        BankAccount ba1=new BankAccount();
        ba1.setIban("ES123456789");
        BankAccount ba2=new BankAccount();
        ba2.setIban("ES987654321");
        BankAccount ba3=new BankAccount();
        ba3.setIban("ES678912345");
        List<BankAccount> entities = Arrays.asList(ba1,ba2,ba3);

        when(bankAccountRepository.findByCompany_IdCompany(idCompany)).thenReturn(entities);

        BankAccountDTO dto1 = new BankAccountDTO();
        dto1.setIban("ES123456789");
        BankAccountDTO dto2 = new BankAccountDTO();
        dto2.setIban("ES987654321");
        BankAccountDTO dto3 = new BankAccountDTO();
        dto3.setIban("ES678912345");
        List<BankAccountDTO> dtos = Arrays.asList(dto1,dto2,dto3);

        when(bankAccountMapper.toBankAccountDTOList(entities)).thenReturn(dtos);

        List<BankAccountDTO> result = bankAccountService.getAllBankAccounts(idCompany);

        verify(ownerValidator).checkOwner(idCompany);
        verify(bankAccountRepository).findByCompany_IdCompany(idCompany);
        verify(bankAccountMapper).toBankAccountDTOList(entities);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getIban()).isEqualTo("ES123456789");
        assertThat(result.get(1).getIban()).isEqualTo("ES987654321");
        assertThat(result.get(2).getIban()).isEqualTo("ES678912345");
    }

    // --- getBankAccount ---
    @Test
    void getBankAccount_shouldReturnBankAccountDTO_whenFound(){
        Company company = new Company();
        company.setIdCompany(1);
        int idBankAccount = 10;

        BankAccount ba = new BankAccount();
        ba.setIban("ES123456789");

        when(bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount,company.getIdCompany())).thenReturn(Optional.of(ba));

        BankAccount result = bankAccountService.getBankAccountByIdAndOwner(idBankAccount,company);

        verify(bankAccountRepository).findByIdBankAccountAndCompany_idCompany(idBankAccount, company.getIdCompany());

        assertThat(result.getIban()).isEqualTo("ES123456789");

    }

    @Test
    void getBankAccount_shouldThrowBankAccountNotFoundException_whenNotFound() {
        Company company = new Company();
        company.setIdCompany(1);
        int idBankAccount = 10;

        when(bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, 1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> bankAccountService.getBankAccountByIdAndOwner(idBankAccount, company))
                .isInstanceOf(BankAccountNotFoundException.class);
    }

    @Test
    void deleteBankAccount_shouldDelete(){
        int idCompany = 1;
        int idBankAccount = 1;
        doNothing().when(ownerValidator).checkOwner(idCompany);

        BankAccount ba = new BankAccount();

        when(bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany)).thenReturn(Optional.of(ba));

        bankAccountService.deleteBankAccount(idCompany, idBankAccount);

        verify(ownerValidator).checkOwner(idCompany);
        verify(bankAccountRepository).findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany);
        verify(bankAccountRepository).delete(ba);
    }

    @Test
    void deleteBankAccount_shouldThrowBankAccountNotFoundException_whenNotFound(){
        int idCompany = 1;
        int idBankAccount = 10;

        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> bankAccountService.deleteBankAccount(idCompany, idBankAccount))
                .isInstanceOf(BankAccountNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(bankAccountRepository).findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany);
        verify(bankAccountRepository, never()).delete(any());
    }

    @Test
    void createBankAccount_shouldSaveBankAccount_whenCompanyExists(){
        int idCompany = 1;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        CreateBankAccountRequestDTO dto = new CreateBankAccountRequestDTO();
        BankAccount entity = new BankAccount();
        Company company = new Company();

        when(companyRepository.findByIdCompany(idCompany)).thenReturn(Optional.of(company));
        when(bankAccountMapper.toEntity(dto)).thenReturn(entity);

        bankAccountService.createBankAccount(idCompany, dto);

        verify(ownerValidator).checkOwner(idCompany);
        verify(companyRepository).findByIdCompany(idCompany);
        verify(bankAccountMapper).toEntity(dto);
        assertThat(entity.getCompany()).isSameAs(company);
        verify(bankAccountRepository).save(entity);
    }

    @Test
    void createBankAccount_shouldThrowCompanyNotFoundException_whenCompanyNotFound(){
        int idCompany = 1;

        doNothing().when(ownerValidator).checkOwner(idCompany);

        CreateBankAccountRequestDTO dto = new CreateBankAccountRequestDTO();
        assertThatThrownBy(() -> bankAccountService.createBankAccount(idCompany,dto))
                .isInstanceOf(CompanyNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(companyRepository).findByIdCompany(idCompany);
        verify(bankAccountMapper, never()).toEntity(dto);
        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    void updateBankAccount_shouldUpdateAndReturnDTO_whenBankAccountPhoneFound(){
        int idCompany = 1;
        int idBankAccount = 10;
        UpdateBankAccountRequestDTO updateDto = new UpdateBankAccountRequestDTO();

        doNothing().when(ownerValidator).checkOwner(idCompany);

        CreateBankAccountRequestDTO createDto = new CreateBankAccountRequestDTO();
        doNothing().when(dtoValidator).validate(createDto);

        BankAccount entity = new BankAccount();
        BankAccount updatedEntity = new BankAccount();
        BankAccountDTO updatedDto = new BankAccountDTO();
        when(bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany)).thenReturn(Optional.of(entity));
        when(bankAccountMapper.toCreateBankAccountRequestDTO(updateDto,entity)).thenReturn(createDto);
        when(bankAccountRepository.save(entity)).thenReturn(updatedEntity);
        when(bankAccountMapper.toBankAccountDTO(updatedEntity)).thenReturn(updatedDto);

        BankAccountDTO result = bankAccountService.updateBankAccount(idCompany, idBankAccount, updateDto);

        verify(ownerValidator).checkOwner(idCompany);
        verify(bankAccountRepository).findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany);
        verify(bankAccountMapper).toCreateBankAccountRequestDTO(updateDto, entity);
        verify(dtoValidator).validate(createDto);
        verify(bankAccountMapper).updateEntity(entity, updateDto);
        verify(bankAccountRepository).save(entity);
        verify(bankAccountMapper).toBankAccountDTO(updatedEntity);

        assertThat(result).isSameAs(updatedDto);
    }

    @Test
    void updateBankAccount_shouldThrowBankAccountNotFoundException_whenNotFound(){
        int idCompany = 1;
        int idBankAccount = 10;
        UpdateBankAccountRequestDTO updateDto = new UpdateBankAccountRequestDTO();

        doNothing().when(ownerValidator).checkOwner(idCompany);
        when(bankAccountRepository.findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> bankAccountService.updateBankAccount(idCompany, idBankAccount, updateDto))
                .isInstanceOf(BankAccountNotFoundException.class);

        verify(ownerValidator).checkOwner(idCompany);
        verify(bankAccountRepository).findByIdBankAccountAndCompany_idCompany(idBankAccount, idCompany);
        verify(bankAccountMapper, never()).toCreateBankAccountRequestDTO(any(), any());
        verify(dtoValidator, never()).validate(any());
        verify(bankAccountMapper, never()).updateEntity(any(), any());
        verify(bankAccountRepository, never()).save(any());
        verify(bankAccountMapper, never()).toBankAccountDTO(any());
    }

}
