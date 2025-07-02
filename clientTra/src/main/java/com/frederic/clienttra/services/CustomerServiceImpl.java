package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.*;
import com.frederic.clienttra.dto.read.*;
import com.frederic.clienttra.dto.update.*;
import com.frederic.clienttra.entities.*;
import com.frederic.clienttra.exceptions.CustomerNotFoundException;
import com.frederic.clienttra.mappers.*;
import com.frederic.clienttra.projections.CustomerListProjection;
import com.frederic.clienttra.projections.CustomerMinimalProjection;
import com.frederic.clienttra.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {//TODO Me falta los esquemas aquí!!

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final CustomerMapper customerMapper;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    @Override
    public List<CustomersForListDTO> getAllCustomers() {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<CustomerListProjection> entities = new ArrayList<>(customerRepository.findListByOwnerCompany(owner));
        List<CustomersForListDTO> customers=customerMapper.customersForListDTOS(entities);
        customers.sort(Comparator.comparing(CustomersForListDTO::getComName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

        return customers;
    }

    @Override
    public List<CustomersForListDTO> searchByNameOrVat(String input) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        String query = "%" + input + "%";

        List<CustomerListProjection> entities = new ArrayList<>(customerRepository.findListByComNameOrLegalNameOrVatNumber(owner, query));
        List<CustomersForListDTO> customers=customerMapper.customersForListDTOS(entities);
        customers.sort(Comparator.comparing(CustomersForListDTO::getComName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

        return customers;
    }

    @Override
    public CustomerDetailsDTO getCustomerById(int id) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Customer customer = customerRepository.findByOwnerCompanyAndIdCustomer(owner, id)
                .orElseThrow(CustomerNotFoundException::new);
        return customerMapper.toCustomerDetailsDTO(customer);
    }

    @Override
    public void createCustomer(CreateCustomerRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Company companyEntity = companyMapper.toEntity(dto);
        companyEntity.setOwnerCompany(owner);

        Company savedCompany = companyRepository.save(companyEntity);

        Customer customer = customerMapper.toEntity(dto);
        customer.setCompany(savedCompany);
        customer.setOwnerCompany(owner);
        customer.setEnabled(true);

        customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(int id, UpdateCustomerRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Customer customer = customerRepository.findByOwnerCompanyAndIdCustomer(owner,id)
                .orElseThrow(CustomerNotFoundException::new);

        Company company = customer.getCompany();

        //TODO Convertir en CreateCustomerRequestDTO y pasar el DtoValidator

        companyMapper.updateEntity(company,dto);
        customerMapper.updateEntity(customer, dto);

        companyRepository.save(company);
        customerRepository.save(customer);
    }

    @Override
    public void disableCustomer(int id) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Customer customer = customerRepository.findByOwnerCompanyAndIdCustomer(owner,id)
                .orElseThrow(CustomerNotFoundException::new);
        customer.setEnabled(false);
        customerRepository.save(customer);
    }

    @Override
    public List<CustomerMinimalDTO> getMinimalCustomerList() {
        Company owner = companyService.getCurrentCompanyOrThrow();
        List<CustomerMinimalProjection> projections = customerRepository.findMinimalListByOwnerCompany(owner);
        return customerMapper.toMinimalDTOs(projections);
    }

}


