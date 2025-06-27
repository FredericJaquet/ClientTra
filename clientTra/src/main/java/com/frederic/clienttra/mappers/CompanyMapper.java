package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.BaseCompanyRequestDTO;
import com.frederic.clienttra.dto.CompanyOwnerDTO;
import com.frederic.clienttra.entities.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CompanyMapper {
    private final AddressMapper addressMapper;
    private final PhoneMapper phoneMapper;
    private final BankAccountMapper bankAccountMapper;
    private final UserMapper userMapper;
    private final ContactPersonMapper contactPersonMapper;

    public CompanyOwnerDTO toCompanyOwnerDTO(Company company) {
        return CompanyOwnerDTO.builder()
                .idCompany(company.getIdCompany())
                .vatNumber(company.getVatNumber())
                .comName(company.getComName())
                .legalName(company.getLegalName())
                .email(company.getEmail())
                .web(company.getWeb())
                .logoPath(company.getLogoPath())
                .addresses(safeMapToDTO(company.getAddresses(), addressMapper::toAddressDTO))
                .phones(safeMapToDTO(company.getPhones(), phoneMapper::toPhoneDTO))
                .bankAccounts(safeMapToDTO(company.getBankAccounts(), bankAccountMapper::toBankAccountDTO))
                .users(safeMapToDTO(company.getUsers(), userMapper::toAdminDTO))
                .build();
    }

    public Company toEntity(BaseCompanyRequestDTO dto) {
        Company company = new Company();

        company.setVatNumber(dto.getVatNumber());
        company.setComName(dto.getComName());
        company.setLegalName(dto.getLegalName());
        company.setEmail(dto.getEmail());
        company.setWeb(dto.getWeb());

        safeMapToEntity(dto.getPhones(), phoneMapper::toEntity)
                .forEach(company::addPhone);

        safeMapToEntity(dto.getAddresses(), addressMapper::toEntity)
                .forEach(company::addAddress);
        safeMapToEntity(dto.getBankAccounts(), bankAccountMapper::toEntity)
                .forEach(company::addBankAccount);

        safeMapToEntity(dto.getContactPersons(), contactPersonMapper::toEntity)
                .forEach(company::addContactPerson);

        return company;
    }

    private <T, R> List<R> safeMapToDTO(List<T> list, Function<T, R> mapper) {
        return list == null ? List.of() :
                list.stream()
                        .filter(Objects::nonNull)
                        .map(mapper)
                        .toList();
    }

    private <T, R> List<R> safeMapToEntity(List<T> list, Function<T, R> mapper) {
        return list == null ? List.of() :
                list.stream()
                        .filter(Objects::nonNull)
                        .map(mapper)
                        .toList();
    }

}
