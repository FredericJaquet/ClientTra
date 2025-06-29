package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreatePhoneRequestDTO;
import com.frederic.clienttra.dto.read.AddressDTO;
import com.frederic.clienttra.dto.read.BankAccountDTO;
import com.frederic.clienttra.dto.read.CompanyOwnerDTO;
import com.frederic.clienttra.dto.read.PhoneDTO;
import com.frederic.clienttra.dto.update.UpdateAddressRequestDTO;
import com.frederic.clienttra.dto.update.UpdateBankAccountRequestDTO;
import com.frederic.clienttra.dto.update.UpdateCompanyOwnerDTO;
import com.frederic.clienttra.dto.update.UpdatePhoneRequestDTO;
import com.frederic.clienttra.entities.Address;
import com.frederic.clienttra.entities.BankAccount;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Phone;
import com.frederic.clienttra.exceptions.CompanyNotFoundForUserException;
import com.frederic.clienttra.exceptions.InvalidIbanException;
import com.frederic.clienttra.exceptions.LogoNotLoadedException;
import com.frederic.clienttra.mappers.AddressMapper;
import com.frederic.clienttra.mappers.BankAccountMapper;
import com.frederic.clienttra.mappers.CompanyMapper;
import com.frederic.clienttra.mappers.PhoneMapper;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.security.CustomUserDetails;
import com.frederic.clienttra.utils.validators.IbanValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @Override
    public Company getCurrentCompanyOrThrow() {
        return getCurrentCompany().orElseThrow(CompanyNotFoundForUserException::new);
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

        updatePhones(dto, company);
        updateBankAccounts(dto, company);
        updateAddresses(dto, company);

        companyRepository.save(company);
    }

    @Override
    public void uploadCompanyLogo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Archivo vacío");
        }

        Company company = getCurrentCompanyOrThrow();

        try {
            String filename = "logo_" + company.getIdCompany() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path destinationPath = Paths.get("uploads/logos", filename).toAbsolutePath().normalize();

            Files.createDirectories(destinationPath.getParent());

            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            // Guardar solo el path relativo
            company.setLogoPath("uploads/logos/" + filename);
            companyRepository.save(company);

        } catch (IOException e) {
            throw new LogoNotLoadedException();
        }
    }

    private void updatePhones(UpdateCompanyOwnerDTO dto, Company company){
        if (dto.getPhones() != null) {
            List<Phone> currentPhones = company.getPhones();

            // Eliminar phones que no están en dto
            currentPhones.removeIf(phone ->
                    dto.getPhones().stream()
                            .noneMatch(dtoPhone -> dtoPhone.getIdPhone() != null && dtoPhone.getIdPhone().equals(phone.getIdPhone()))
            );

            for (UpdatePhoneRequestDTO phoneDTO : dto.getPhones()) {
                if (phoneDTO.getIdPhone() == null) {
                    // Nuevo phone - Validar primero
                    CreatePhoneRequestDTO createPhoneDTO = phoneMapper.toCreatePhoneRequestDTO(phoneDTO);
                    Phone newPhone = phoneMapper.toEntity(createPhoneDTO);
                    company.addPhone(newPhone);
                } else {
                    // Actualizar existente
                    currentPhones.stream()
                            .filter(p -> Objects.equals(p.getIdPhone(), phoneDTO.getIdPhone()))
                            .findFirst().ifPresent(existingPhone -> phoneMapper.updateEntity(existingPhone, phoneDTO));
                }
            }
        }
    }

    private void updateBankAccounts(UpdateCompanyOwnerDTO dto, Company company){
        if (dto.getBankAccounts() != null) {
            List<BankAccount> currentAccounts = company.getBankAccounts();

            // Eliminar cuentas que no están en dto
            currentAccounts.removeIf(account ->
                    dto.getBankAccounts().stream()
                            .noneMatch(dtoAccount -> dtoAccount.getIdBankAccount() != null &&
                                    dtoAccount.getIdBankAccount().equals(account.getIdBankAccount()))
            );

            for (UpdateBankAccountRequestDTO accountDTO : dto.getBankAccounts()) {
                if (!IbanValidator.isValidIban(accountDTO.getIban())) {
                    throw new InvalidIbanException();
                }
                if (accountDTO.getIdBankAccount() == null) {
                    // Nueva cuenta
                    BankAccount newAccount = bankAccountMapper.toEntity(accountDTO);
                    company.addBankAccount(newAccount);
                } else {
                    // Actualizar existente
                    currentAccounts.stream()
                            .filter(acc -> acc.getIdBankAccount().equals(accountDTO.getIdBankAccount()))
                            .findFirst().ifPresent(existingAccount -> bankAccountMapper.updateEntity(existingAccount, accountDTO));
                }
            }
        }
    }

    private void updateAddresses(UpdateCompanyOwnerDTO dto, Company company){
        if (dto.getAddresses() != null) {
            List<Address> currentAddresses = company.getAddresses();
            List<Integer> incomingIds = dto.getAddresses().stream()
                    .map(UpdateAddressRequestDTO::getIdAddress)
                    .filter(Objects::nonNull)
                    .toList();

            // Eliminar las direcciones que ya no están
            currentAddresses.removeIf(address ->
                    address.getIdAddress() != null && !incomingIds.contains(address.getIdAddress()));

            for (UpdateAddressRequestDTO addressDTO : dto.getAddresses()) {
                if (addressDTO.getIdAddress() == null) {
                    // Nueva dirección
                    Address newAddress = addressMapper.toEntity(addressDTO);
                    company.addAddress(newAddress);
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
