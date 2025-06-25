package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.CompanyOwnerDTO;
import com.frederic.clienttra.entities.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CompanyMapper {
    private final AddressMapper addressMapper;
    private final PhoneMapper phoneMapper;
    private final BankAccountMapper bankAccountMapper;
    private final UserMapper userMapper;

    public CompanyOwnerDTO toCompanyOwnerDTO(Company company) {
        try {
            return CompanyOwnerDTO.builder()
                    .idCompany(company.getIdCompany())
                    .vatNumber(company.getVatNumber())
                    .comName(company.getComName())
                    .legalName(company.getLegalName())
                    .email(company.getEmail())
                    .web(company.getWeb())
                    .logoPath(company.getLogoPath())
                    .addresses(company.getAddresses() == null ? List.of() :
                            company.getAddresses().stream()
                                    .filter(Objects::nonNull)
                                    .map(addressMapper::toAddressDTO)
                                    .toList())
                    .phones(company.getPhones() == null ? List.of() :
                            company.getPhones().stream()
                                    .filter(Objects::nonNull)
                                    .map(phoneMapper::toPhoneDTO)
                                    .toList())
                    .bankAccounts(company.getBankAccounts() == null ? List.of() :
                            company.getBankAccounts().stream()
                                    .filter(Objects::nonNull)
                                    .map(bankAccountMapper::toBankAccountDTO)
                                    .toList())
                    .users(company.getUsers() == null ? List.of() :
                            company.getUsers().stream()
                                    .filter(Objects::nonNull)
                                    .map(userMapper::toAdminDTO)
                                    .toList())
                    .build();
        } catch (NullPointerException e) {
            e.printStackTrace(); // o log error
            throw e; // vuelve a lanzar para no silenciarlo
        }
    }
}
