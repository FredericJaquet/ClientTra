package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.*;
import com.frederic.clienttra.entities.Address;
import com.frederic.clienttra.entities.BankAccount;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Phone;
import com.frederic.clienttra.exceptions.CompanyNotFoundForUserException;
import com.frederic.clienttra.mappers.AddressMapper;
import com.frederic.clienttra.mappers.BankAccountMapper;
import com.frederic.clienttra.mappers.CompanyMapper;
import com.frederic.clienttra.mappers.PhoneMapper;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final PhoneMapper phoneMapper;
    private final BankAccountMapper bankAccountMapper;
    private final AddressMapper addressMapper;

    @Override
    public Optional<Company> getCurrentCompany() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            int currentCompanyId = userDetails.getIdCompany();
            return companyRepository.findById(currentCompanyId);
        }

        return Optional.empty();
    }

    public Optional<CompanyOwnerDTO> getCompanyOwner(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            int currentCompanyId = userDetails.getIdCompany();
            return companyRepository.findById(currentCompanyId)
                    .map(companyMapper::toCompanyOwnerDTO);
        }
        return Optional.empty();
    }

    @Override
    public Company getCurrentCompanyOrThrow() {
        return getCurrentCompany().orElseThrow(CompanyNotFoundForUserException::new);
    }

    @Override
    public void updateCompanyOwner(UpdateCompanyOwnerDTO dto) {
        Company company = getCurrentCompanyOrThrow();
        if(dto.getVatNumber()!=null){
            company.setVatNumber(dto.getVatNumber());
        }
        if(dto.getComName()!=null){
            company.setComName(dto.getComName());
        }
        if(dto.getLegalName()!=null){
            company.setLegalName(dto.getLegalName());
        }
        if(dto.getEmail()!=null){
            company.setEmail(dto.getEmail());
        }
        if(dto.getWeb()!=null){
            company.setWeb(dto.getWeb());
        }
        if(dto.getLogoPath()!=null){
            company.setLogoPath(dto.getLogoPath());
        }

        checkPhones(dto, company);
        checkBankAccounts(dto, company);
        checkAddresses(dto, company);

        companyRepository.save(company);
    }

    private void checkPhones(UpdateCompanyOwnerDTO dto, Company company){
        if (dto.getPhones() != null) {
            List<Phone> currentPhones = company.getPhones();

            // Eliminar phones que no están en dto
            currentPhones.removeIf(phone ->
                    dto.getPhones().stream()
                            .noneMatch(dtoPhone -> dtoPhone.getIdPhone() != null && dtoPhone.getIdPhone().equals(phone.getIdPhone()))
            );

            for (PhoneDTO phoneDTO : dto.getPhones()) {
                if (phoneDTO.getIdPhone() == null) {
                    // Nuevo phone
                    Phone newPhone = phoneMapper.toEntity(phoneDTO);
                    newPhone.setCompany(company);
                    currentPhones.add(newPhone);
                } else {
                    // Actualizar existente
                    currentPhones.stream()
                            .filter(p -> p.getIdPhone() == phoneDTO.getIdPhone())
                            .findFirst().ifPresent(existingPhone -> phoneMapper.updateEntity(existingPhone, phoneDTO));
                }
            }
        }
    }

    private void checkBankAccounts(UpdateCompanyOwnerDTO dto, Company company){
        if (dto.getBankAccounts() != null) {
            List<BankAccount> currentAccounts = company.getBankAccounts();

            // Eliminar cuentas que no están en dto
            currentAccounts.removeIf(account ->
                    dto.getBankAccounts().stream()
                            .noneMatch(dtoAccount -> dtoAccount.getIdBankAccount() != null &&
                                    dtoAccount.getIdBankAccount().equals(account.getIdBankAccount()))
            );

            for (BankAccountDTO accountDTO : dto.getBankAccounts()) {
                if (accountDTO.getIdBankAccount() == null) {
                    // Nueva cuenta
                    BankAccount newAccount = bankAccountMapper.toEntity(accountDTO);
                    newAccount.setCompany(company);
                    currentAccounts.add(newAccount);
                } else {
                    // Actualizar existente
                    currentAccounts.stream()
                            .filter(acc -> acc.getIdBankAccount().equals(accountDTO.getIdBankAccount()))
                            .findFirst().ifPresent(existingAccount -> bankAccountMapper.updateEntity(existingAccount, accountDTO));
                }
            }
        }
    }

    private void checkAddresses(UpdateCompanyOwnerDTO dto, Company company){
        if (dto.getAddresses() != null) {
            List<Address> currentAddresses = company.getAddresses();
            List<Integer> incomingIds = dto.getAddresses().stream()
                    .map(AddressDTO::getIdAddress)
                    .filter(Objects::nonNull)
                    .toList();

            // Eliminar las direcciones que ya no están
            currentAddresses.removeIf(address ->
                    address.getIdAddress() != null && !incomingIds.contains(address.getIdAddress()));

            for (AddressDTO addressDTO : dto.getAddresses()) {
                if (addressDTO.getIdAddress() == null) {
                    // Nueva dirección
                    Address newAddress = addressMapper.toEntity(addressDTO);
                    newAddress.setCompany(company);
                    currentAddresses.add(newAddress);
                } else {
                    // Actualizar existente
                    currentAddresses.stream()
                            .filter(a -> Objects.equals(a.getIdAddress(), addressDTO.getIdAddress()))
                            .findFirst()
                            .ifPresent(existing -> addressMapper.updateEntity(existing, addressDTO));
                }
            }
        }

    }

}
