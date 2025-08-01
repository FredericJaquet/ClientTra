package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.create.CreateBaseCompanyRequestDTO;
import com.frederic.clienttra.dto.demo.DemoCompanyDTO;
import com.frederic.clienttra.dto.read.BaseCompanyMinimalDTO;
import com.frederic.clienttra.dto.read.CompanyOwnerDTO;
import com.frederic.clienttra.dto.update.UpdateBaseCompanyRequestDTO;
import com.frederic.clienttra.dto.update.UpdateCompanyOwnerRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.InvalidEmailException;
import com.frederic.clienttra.exceptions.InvalidLegalNameException;
import com.frederic.clienttra.exceptions.InvalidVatNumberException;
import com.frederic.clienttra.validators.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper class responsible for converting between Company entities and various Company-related DTOs.
 * <p>
 * Handles mappings for creating, updating, reading, and demo data of Company,
 * including nested entities such as addresses, phones, bank accounts, users, contact persons, documents, orders, and schemes.
 * Includes validation checks when updating entities.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class CompanyMapper {
    private final AddressMapper addressMapper;
    private final PhoneMapper phoneMapper;
    private final BankAccountMapper bankAccountMapper;
    private final UserMapper userMapper;
    private final ContactPersonMapper contactPersonMapper;
    private final DocumentMapper documentMapper;
    private final SchemeMapper schemeMapper;
    private final OrderMapper orderMapper;

    /**
     * Converts a Company entity into a CompanyOwnerDTO including related entities.
     *
     * @param entity the Company entity to convert
     * @return a CompanyOwnerDTO with detailed company info and related nested DTOs
     */
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

    /**
     * Converts a CreateBaseCompanyRequestDTO into a new Company entity,
     * including nested entities like phones, addresses, bank accounts, and contact persons.
     *
     * @param dto the DTO containing data for the new Company
     * @return a Company entity constructed from the DTO
     */
    public Company toEntity(CreateBaseCompanyRequestDTO dto) {
        Company company = Company.builder()
                .comName(dto.getComName())
                .legalName(dto.getLegalName())
                .vatNumber(dto.getVatNumber())
                .email(dto.getEmail())
                .web(dto.getWeb())
                .build();

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

    /**
     * Converts a DemoCompanyDTO into a Company entity linked to the given ownerCompany,
     * including nested entities such as documents, orders, schemes, phones, addresses, bank accounts, and contact persons.
     *
     * @param dto the DemoCompanyDTO to convert
     * @param ownerCompany the owning Company entity
     * @return a Company entity built from the demo DTO and associated with ownerCompany
     */
    public Company toEntity(DemoCompanyDTO dto, Company ownerCompany) {
        Company company = Company.builder()
                .comName(dto.getComName())
                .legalName(dto.getLegalName())
                .vatNumber(dto.getVatNumber())
                .email(dto.getEmail())
                .web(dto.getWeb())
                .ownerCompany(ownerCompany)
                .build();

        safeMapToEntity(dto.getDocuments(), docDto -> documentMapper.toEntity(docDto, ownerCompany, company))
                .forEach(company::addDocument);
        safeMapToEntity(dto.getOrders(), orderDto -> orderMapper.toEntity(orderDto, ownerCompany, company))
                .forEach(company::addOrder);
        safeMapToEntity(dto.getSchemes(), schemeDto -> schemeMapper.toEntity(schemeDto, ownerCompany))
                .forEach(company::addScheme);
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

    /**
     * Converts a list of DemoCompanyDTOs into a list of Company entities linked to the given ownerCompany.
     *
     * @param dtos list of DemoCompanyDTO objects to convert
     * @param ownerCompany the owning Company entity
     * @return a list of Company entities associated with ownerCompany
     */
    public List<Company> toEntities(List<DemoCompanyDTO> dtos, Company ownerCompany){
        return dtos.stream()
                .map(dto -> toEntity(dto, ownerCompany))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Updates an existing Company entity using data from an UpdateBaseCompanyRequestDTO,
     * including validation for VAT number, legal name, and email format.
     *
     * @param entity the Company entity to update
     * @param dto the DTO containing updated fields (nullable)
     * @throws InvalidVatNumberException if VAT number is invalid
     * @throws InvalidLegalNameException if legal name is invalid
     * @throws InvalidEmailException if email format is invalid
     */
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

    /**
     * Updates an existing Company entity using data from an UpdateCompanyOwnerRequestDTO,
     * including validation for VAT number, legal name, email format, and setting the logo path.
     *
     * @param entity the Company entity to update
     * @param dto the DTO containing updated fields (nullable)
     * @throws InvalidVatNumberException if VAT number is invalid
     * @throws InvalidLegalNameException if legal name is invalid
     * @throws InvalidEmailException if email format is invalid
     */
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

    /**
     * Converts a Company entity into a minimal DTO containing only id, VAT number, and commercial name.
     *
     * @param entity the Company entity to convert
     * @return a BaseCompanyMinimalDTO with minimal company data
     */
    public BaseCompanyMinimalDTO toBaseCompanyMinimalDTO(Company entity){
        return BaseCompanyMinimalDTO.builder()
                .idCompany(entity.getIdCompany())
                .vatNumber(entity.getVatNumber())
                .comName(entity.getComName())
                .build();
    }

    /**
     * Helper method to safely map a list of entities to DTOs,
     * returning an empty list if the input is null.
     *
     * @param list the input list to map (may be null)
     * @param mapper the function that maps an element to its DTO
     * @param <T> the input list element type
     * @param <R> the output DTO type
     * @return a list of mapped DTOs, or empty list if input is null
     */
    private <T, R> List<R> safeMapToDTO(List<T> list, Function<T, R> mapper) {
        return list == null ? List.of() :
                list.stream()
                        .filter(Objects::nonNull)
                        .map(mapper)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Helper method to safely map a list of DTOs to entities,
     * returning an empty list if the input is null.
     *
     * @param list the input list to map (may be null)
     * @param mapper the function that maps an element to its entity
     * @param <T> the input list element type
     * @param <R> the output entity type
     * @return a list of mapped entities, or empty list if input is null
     */
    private <T, R> List<R> safeMapToEntity(List<T> list, Function<T, R> mapper) {
        return list == null ? List.of() :
                list.stream()
                        .filter(Objects::nonNull)
                        .map(mapper)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

}
