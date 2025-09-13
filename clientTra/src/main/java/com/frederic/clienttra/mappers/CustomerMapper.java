package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.create.CreateCustomerRequestDTO;
import com.frederic.clienttra.dto.demo.DemoCompanyDTO;
import com.frederic.clienttra.dto.read.CustomerDetailsDTO;
import com.frederic.clienttra.dto.read.BaseCompanyMinimalDTO;
import com.frederic.clienttra.dto.read.CustomerForListDTO;
import com.frederic.clienttra.dto.update.UpdateCustomerRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Customer;
import com.frederic.clienttra.projections.CustomerListProjection;
import com.frederic.clienttra.projections.CustomerMinimalProjection;
import com.frederic.clienttra.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper class responsible for converting between Customer entities and their various DTO representations.
 * <p>
 * Supports conversions for listing customers, detailed views, creating and updating entities,
 * as well as handling DemoCompanyDTO conversions.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final AddressMapper addressMapper;
    private final CompanyMapper companyMapper;
    private final PhoneMapper phoneMapper;
    private final BankAccountMapper bankAccountMapper;
    private final ContactPersonMapper contactPersonMapper;
    private final SchemeMapper schemeMapper;
    private final CompanyRepository companyRepository;

    /**
     * Converts a list of CustomerListProjection to a list of CustomerForListDTO.
     *
     * @param entities the list of CustomerListProjection to convert
     * @return a list of CustomerForListDTO representing customers for listing
     */
    public List<CustomerForListDTO> toCustomerForListDTOS(List<CustomerListProjection> entities){
        return entities.stream()
                .map(p -> CustomerForListDTO.builder()
                        .idCustomer(p.getIdCustomer())
                        .comName(p.getComName())
                        .vatNumber(p.getVatNumber())
                        .email(p.getEmail())
                        .web(p.getWeb())
                        .enabled(p.getEnabled())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a Customer entity to a detailed CustomerDetailsDTO including related company data.
     *
     * @param entity the Customer entity to convert
     * @return a CustomerDetailsDTO with full customer details
     */
    public CustomerDetailsDTO toCustomerDetailsDTO(Customer entity) {
        return CustomerDetailsDTO.builder()
                .idCompany(entity.getCompany().getIdCompany())
                .idCustomer(entity.getIdCustomer())
                .comName(entity.getCompany().getComName())
                .legalName(entity.getCompany().getLegalName())
                .web(entity.getCompany().getWeb())
                .email(entity.getCompany().getEmail())
                .invoicingMethod(entity.getInvoicingMethod())
                .payMethod(entity.getPayMethod())
                .defaultLanguage(entity.getDefaultLanguage())
                .vatNumber(entity.getCompany().getVatNumber())
                .defaultVat(entity.getDefaultVat())
                .defaultWithholding(entity.getDefaultWithholding())
                .duedate(entity.getDuedate())
                .europe(entity.getEurope())
                .enabled(entity.getEnabled())
                .addresses(safeMapToDTO(entity.getCompany().getAddresses(), addressMapper::toAddressDTO))
                .phones(safeMapToDTO(entity.getCompany().getPhones(), phoneMapper::toPhoneDTO))
                .bankAccounts(safeMapToDTO(entity.getCompany().getBankAccounts(), bankAccountMapper::toBankAccountDTO))
                .contactPersons(safeMapToDTO(entity.getCompany().getContactPersons(), contactPersonMapper::toContactPersonDTO))
                .schemes(safeMapToDTO(entity.getCompany().getSchemes(), schemeMapper::toDto))
                .build();
    }

    /**
     * Converts a CreateCustomerRequestDTO to a Customer entity.
     *
     * @param dto the DTO containing creation data
     * @return a new Customer entity with the provided data
     */
    public Customer toEntity(CreateCustomerRequestDTO dto) {
        return Customer.builder()
                .invoicingMethod(dto.getInvoicingMethod())
                .duedate(dto.getDuedate())
                .payMethod(dto.getPayMethod())
                .defaultLanguage(dto.getDefaultLanguage())
                .defaultVat(dto.getDefaultVAT())
                .defaultWithholding(dto.getDefaultWithholding())
                .europe(dto.getEurope())
                .enabled(true)
                .build();
    }

    /**
     * Converts a DemoCompanyDTO into a Customer entity, creating and saving the associated Company first.
     *
     * @param dto the demo company DTO with data
     * @param ownerCompany the owner company entity for ownership reference
     * @return a Customer entity linked to the saved Company
     */
    public Customer toEntity(DemoCompanyDTO dto, Company ownerCompany) {
        Company company = companyMapper.toEntity(dto,ownerCompany);
        Company savedCompany =  companyRepository.save(company);

        return Customer.builder()
                .invoicingMethod(dto.getInvoicingMethod())
                .duedate(dto.getDuedate())
                .payMethod(dto.getPayMethod())
                .defaultLanguage(dto.getDefaultLanguage())
                .defaultVat(dto.getDefaultVAT())
                .defaultWithholding(dto.getDefaultWithholding())
                .europe(dto.getEurope())
                .enabled(true)
                .company(savedCompany)
                .ownerCompany(ownerCompany)
                .build();
    }

    /**
     * Converts a list of DemoCompanyDTOs into Customer entities associated with the given owner company.
     *
     * @param dtos the list of demo company DTOs
     * @param ownerCompany the owner company entity
     * @return list of Customer entities created from the DTOs
     */
    public List<Customer> toEntities(List<DemoCompanyDTO> dtos, Company ownerCompany){
        return dtos.stream()
                .map(dto -> toEntity(dto, ownerCompany))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Updates a Customer entity with non-null values from an UpdateCustomerRequestDTO.
     *
     * @param entity the Customer entity to update
     * @param dto the DTO containing updated data (nullable fields)
     */
    public void updateEntity(Customer entity, UpdateCustomerRequestDTO dto) {
        if (dto.getInvoicingMethod() != null) {
            entity.setInvoicingMethod(dto.getInvoicingMethod());
        }
        if (dto.getDuedate() != null) {
            entity.setDuedate(dto.getDuedate());
        }
        if (dto.getPayMethod() != null) {
            entity.setPayMethod(dto.getPayMethod());
        }
        if (dto.getDefaultLanguage() != null) {
            entity.setDefaultLanguage(dto.getDefaultLanguage());
        }
        if (dto.getDefaultVat() != null) {
            entity.setDefaultVat(dto.getDefaultVat());
        }
        if (dto.getDefaultWithholding() != null) {
            entity.setDefaultWithholding(dto.getDefaultWithholding());
        }
        if (dto.getEurope() != null) {
            entity.setEurope(dto.getEurope());
        }
        if (dto.getEnabled() != null) {
            entity.setEnabled(dto.getEnabled());
        }
    }

    /**
     * Converts a CustomerMinimalProjection to a BaseCompanyMinimalDTO.
     *
     * @param entity the projection containing minimal customer data
     * @return a BaseCompanyMinimalDTO with minimal company info
     */
    public BaseCompanyMinimalDTO toMinimalDTO(CustomerMinimalProjection entity) {
        return BaseCompanyMinimalDTO.builder()
                .idCompany(entity.getIdCompany())
                .comName(entity.getComName())
                .vatNumber(entity.getVatNumber())
                .build();
    }

    /**
     * Converts a list of CustomerMinimalProjection entities to a list of BaseCompanyMinimalDTO.
     *
     * @param entities the list of minimal customer projections
     * @return a list of minimal company DTOs
     */
    public List<BaseCompanyMinimalDTO> toMinimalDTOs(List<CustomerMinimalProjection> entities) {
        return entities.stream()
                .map(this::toMinimalDTO)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Helper method to safely map a list using a mapper function, handling null or empty lists.
     *
     * @param list the input list, which can be null
     * @param mapper the function to map each element
     * @param <T> the input list element type
     * @param <R> the output list element type
     * @return a list of mapped elements or an empty list if input is null
     */
    private <T, R> List<R> safeMapToDTO(List<T> list, Function<T, R> mapper) {
        return list == null ? List.of() :
                list.stream()
                        .filter(Objects::nonNull)
                        .map(mapper)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

}
