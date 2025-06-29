package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.create.CreateCustomerRequestDTO;
import com.frederic.clienttra.dto.read.CustomerDetailsDTO;
import com.frederic.clienttra.dto.read.CustomersForListDTO;
import com.frederic.clienttra.dto.update.UpdateCustomerRequestDTO;
import com.frederic.clienttra.entities.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final AddressMapper addressMapper;
    private final PhoneMapper phoneMapper;
    private final BankAccountMapper bankAccountMapper;
    private final ContactPersonMapper contactPersonMapper;

    public CustomerDetailsDTO toCustomerDetailsDTO(Customer customer) {
        return CustomerDetailsDTO.builder()
                .idCustomer(customer.getIdCustomer())
                .comName(customer.getCompany().getComName())
                .legaName(customer.getCompany().getLegalName())
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
                .addresses(customer.getCompany().getAddresses() == null ? List.of() :
                        customer.getCompany().getAddresses().stream()
                                .filter(Objects::nonNull)
                                .map(addressMapper::toAddressDTO)
                                .toList())
                .contactPersons(customer.getCompany().getContactPersons() == null ? List.of() :
                        customer.getCompany().getContactPersons().stream()
                                .filter(Objects::nonNull)
                                .map(contactPersonMapper::toContactPersonDTO)
                                .toList())
                .phones(customer.getCompany().getPhones() == null ? List.of() :
                        customer.getCompany().getPhones().stream()
                                .filter(Objects::nonNull)
                                .map(phoneMapper::toPhoneDTO)
                                .toList())
                .bankAccounts(customer.getCompany().getBankAccounts() == null ? List.of() :
                        customer.getCompany().getBankAccounts().stream()
                                .filter(Objects::nonNull)
                                .map(bankAccountMapper::toBankAccountDTO)
                                .toList())
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
                        .toList();
    }

}
