package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.create.CreateCustomerRequestDTO;
import com.frederic.clienttra.dto.read.CustomerDetailsDTO;
import com.frederic.clienttra.dto.read.CustomersForListDTO;
import com.frederic.clienttra.dto.update.UpdateCustomerRequestDTO;
import com.frederic.clienttra.entities.Customer;
import com.frederic.clienttra.projections.CustomerListProjection;
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

    public List<CustomersForListDTO> customersForListDTOS(List<CustomerListProjection> entities){
        return entities.stream()
                .map(p -> CustomersForListDTO.builder()
                        .idCustomer(p.getIdCustomer())
                        .comName(p.getComName())
                        .vatNumber(p.getVatNumber())
                        .email(p.getEmail())
                        .web(p.getWeb())
                        .enabled(p.getEnabled())
                        .build())
                .collect(Collectors.toList());
    }

    public CustomerDetailsDTO toCustomerDetailsDTO(Customer customer) {
        return CustomerDetailsDTO.builder()
                .idCustomer(customer.getIdCustomer())
                .comName(customer.getCompany().getComName())
                .legalName(customer.getCompany().getLegalName())
                .web(customer.getCompany().getWeb())
                .email(customer.getCompany().getEmail())
                .invoicingMethod(customer.getInvoicingMethod())
                .payMethod(customer.getPayMethod())
                .defaultLanguage(customer.getDefaultLanguage())
                .vatNumber(customer.getCompany().getVatNumber())
                .defaultVat(customer.getDefaultVat())
                .defaultWithholding(customer.getDefaultWithholding())
                .duedate(customer.getDuedate())
                .europe(customer.getEurope())
                .enabled(customer.getEnabled())
                .addresses(safeMapToDTO(customer.getCompany().getAddresses(), addressMapper::toAddressDTO))
                .phones(safeMapToDTO(customer.getCompany().getPhones(), phoneMapper::toPhoneDTO))
                .bankAccounts(safeMapToDTO(customer.getCompany().getBankAccounts(), bankAccountMapper::toBankAccountDTO))
                .contactPersons(safeMapToDTO(customer.getCompany().getContactPersons(), contactPersonMapper::toContactPersonDTO))
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

    public void updateEntity(Customer customer, UpdateCustomerRequestDTO dto) {
        if (dto.getInvoicingMethod() != null) {
            customer.setInvoicingMethod(dto.getInvoicingMethod());
        }
        if (dto.getDuedate() != null) {
            customer.setDuedate(dto.getDuedate());
        }
        if (dto.getPayMethod() != null) {
            customer.setPayMethod(dto.getPayMethod());
        }
        if (dto.getDefaultLanguage() != null) {
            customer.setDefaultLanguage(dto.getDefaultLanguage());
        }
        if (dto.getDefaultVAT() != null) {
            customer.setDefaultVat(dto.getDefaultVAT());
        }
        if (dto.getDefaultWithholding() != null) {
            customer.setDefaultWithholding(dto.getDefaultWithholding());
        }
        if (dto.getEurope() != null) {
            customer.setEurope(dto.getEurope());
        }
        if (dto.getEnabled() != null) {
            customer.setEnabled(dto.getEnabled());
        }
    }

    private <T, R> List<R> safeMapToDTO(List<T> list, Function<T, R> mapper) {
        return list == null ? List.of() :
                list.stream()
                        .filter(Objects::nonNull)
                        .map(mapper)
                        .collect(Collectors.toList());
    }

}
