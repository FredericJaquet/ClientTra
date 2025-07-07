package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.create.CreateCustomerRequestDTO;
import com.frederic.clienttra.dto.read.CustomerDetailsDTO;
import com.frederic.clienttra.dto.read.BaseCompanyMinimalDTO;
import com.frederic.clienttra.dto.read.CustomerForListDTO;
import com.frederic.clienttra.dto.update.UpdateCustomerRequestDTO;
import com.frederic.clienttra.entities.Customer;
import com.frederic.clienttra.projections.CustomerListProjection;
import com.frederic.clienttra.projections.CustomerMinimalProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final AddressMapper addressMapper;
    private final PhoneMapper phoneMapper;
    private final BankAccountMapper bankAccountMapper;
    private final ContactPersonMapper contactPersonMapper;
    private final SchemeMapper schemeMapper;

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
                .collect(Collectors.toList());
    }

    public CustomerDetailsDTO toCustomerDetailsDTO(Customer entity) {
        return CustomerDetailsDTO.builder()
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
        if (dto.getDefaultVAT() != null) {
            entity.setDefaultVat(dto.getDefaultVAT());
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

    public BaseCompanyMinimalDTO toMinimalDTO(CustomerMinimalProjection entity) {
        return BaseCompanyMinimalDTO.builder()
                .idCompany(entity.getIdCompany())
                .comName(entity.getComName())
                .vatNumber(entity.getVatNumber())
                .build();
    }

    public List<BaseCompanyMinimalDTO> toMinimalDTOs(List<CustomerMinimalProjection> entities) {
        return entities.stream()
                .map(this::toMinimalDTO)
                .toList();
    }

    private <T, R> List<R> safeMapToDTO(List<T> list, Function<T, R> mapper) {
        return list == null ? List.of() :
                list.stream()
                        .filter(Objects::nonNull)
                        .map(mapper)
                        .collect(Collectors.toList());
    }

}
