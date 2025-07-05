package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.create.CreateBaseCompanyRequestDTO;
import com.frederic.clienttra.dto.read.CompanyOwnerDTO;
import com.frederic.clienttra.dto.update.UpdateBaseCompanyRequestDTO;
import com.frederic.clienttra.dto.update.UpdateCompanyOwnerRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.InvalidEmailException;
import com.frederic.clienttra.exceptions.InvalidLegalNameException;
import com.frederic.clienttra.exceptions.InvalidVatNumberException;
import com.frederic.clienttra.utils.validators.EmailValidator;
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

    public CompanyOwnerDTO toCompanyOwnerDTO(Company entity) {
        return CompanyOwnerDTO.builder()
                .idCompany(entity.getIdCompany())
                .vatNumber(entity.getVatNumber())
                .comName(entity.getComName())
                .legalName(entity.getLegalName())
                .email(entity.getEmail())
                .web(entity.getWeb())
                .logoPath(entity.getLogoPath())
                .addresses(safeMapToDTO(entity.getAddresses(), addressMapper::toAddressDTO))
                .phones(safeMapToDTO(entity.getPhones(), phoneMapper::toPhoneDTO))
                .bankAccounts(safeMapToDTO(entity.getBankAccounts(), bankAccountMapper::toBankAccountDTO))
                .users(safeMapToDTO(entity.getUsers(), userMapper::toAdminDTO))
                .build();
    }

    public Company toEntity(CreateBaseCompanyRequestDTO dto) {
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

    public void updateEntity(Company entity, UpdateBaseCompanyRequestDTO dto){
        if(dto.getVatNumber()!=null){
            if (dto.getVatNumber().length() < 3){
                throw new InvalidVatNumberException();
            }
            entity.setVatNumber(dto.getVatNumber());
        }
        if(dto.getComName()!=null){
            entity.setComName(dto.getComName());
        }
        if(dto.getLegalName()!=null){
            if(dto.getLegalName().length() < 3){
                throw new InvalidLegalNameException();
            }
            entity.setLegalName(dto.getLegalName());
        }
        if(dto.getEmail()!=null){
            if(!EmailValidator.isValidEmail(dto.getEmail())){
                throw new InvalidEmailException();
            }
            entity.setEmail(dto.getEmail());
        }
        if(dto.getWeb()!=null){
            entity.setWeb(dto.getWeb());
        }
    }

    public void updateEntity(Company entity, UpdateCompanyOwnerRequestDTO dto){
        if(dto.getVatNumber()!=null){
            if (dto.getVatNumber().length() < 3){
                throw new InvalidVatNumberException();
            }
            entity.setVatNumber(dto.getVatNumber());
        }
        if(dto.getComName()!=null){
            entity.setComName(dto.getComName());
        }
        if(dto.getLegalName()!=null){
            if(dto.getLegalName().length() < 3){
                throw new InvalidLegalNameException();
            }
            entity.setLegalName(dto.getLegalName());
        }
        if(dto.getEmail()!=null){
            if(!EmailValidator.isValidEmail(dto.getEmail())){
                throw new InvalidEmailException();
            }
            entity.setEmail(dto.getEmail());
        }
        if(dto.getWeb()!=null){
            entity.setWeb(dto.getWeb());
        }
        if(dto.getLogoPath()!=null){
            entity.setLogoPath(dto.getLogoPath());
        }
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
