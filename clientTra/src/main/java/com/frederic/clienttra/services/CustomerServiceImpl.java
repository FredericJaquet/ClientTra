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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Implementation of CustomerService interface.
 * Provides business logic for customer-related operations.
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final CustomerMapper customerMapper;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    /**
     * Retrieves all enabled customers of the current user's company, sorted by company name.
     *
     * @return a list of CustomerForListDTO with all enabled customers
     */
    @Transactional(readOnly = true)
    @Override
    public List<CustomerForListDTO> getAllCustomers() {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<CustomerListProjection> entities = new ArrayList<>(customerRepository.findListByOwnerCompany(owner));
        List<CustomerForListDTO> dtos = customerMapper.toCustomerForListDTOS(entities);
        dtos.sort(Comparator.comparing(CustomerForListDTO::getComName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

        return dtos;
    }

    /**
     * Retrieves customers filtered by their enabled status, sorted by company name.
     *
     * @param enabled true to get enabled customers, false to get disabled customers
     * @return a list of filtered CustomerForListDTOs
     */
    @Transactional(readOnly = true)
    public List<CustomerForListDTO> getAllCustomersEnabled(boolean enabled) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<CustomerListProjection> entities = new ArrayList<>(customerRepository.findListByOwnerCompany(owner, enabled));
        List<CustomerForListDTO> dtos = customerMapper.toCustomerForListDTOS(entities);
        dtos.sort(Comparator.comparing(CustomerForListDTO::getComName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

        return dtos;
    }

    /**
     * Retrieves detailed information for a customer by its ID.
     *
     * @param id the customer ID
     * @return CustomerDetailsDTO with detailed customer information
     * @throws CustomerNotFoundException if customer is not found or inaccessible
     */
    @Transactional(readOnly = true)
    @Override
    public CustomerDetailsDTO getCustomerById(int id) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Customer entity = customerRepository.findByOwnerCompanyAndIdCustomer(owner, id)
                .orElseThrow(CustomerNotFoundException::new);
        return customerMapper.toCustomerDetailsDTO(entity);
    }

    /**
     * Searches customers by matching input text against company name, legal name, or VAT number.
     *
     * @param input the search query string
     * @return a list of matching CustomerForListDTOs sorted by company name
     */
    @Transactional(readOnly = true)
    @Override
    public List<CustomerForListDTO> searchByNameOrVat(String input) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        String query = "%" + input + "%";

        List<CustomerListProjection> entities = new ArrayList<>(customerRepository.findListByComNameOrLegalNameOrVatNumber(owner, query));
        List<CustomerForListDTO> dtos = customerMapper.toCustomerForListDTOS(entities);
        dtos.sort(Comparator.comparing(CustomerForListDTO::getComName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

        return dtos;
    }

    /**
     * Retrieves a minimal list of customers for lightweight display purposes (e.g., dropdowns).
     *
     * @return list of BaseCompanyMinimalDTO containing essential customer info
     */
    @Transactional(readOnly = true)
    @Override
    public List<BaseCompanyMinimalDTO> getMinimalCustomerList() {
        Company owner = companyService.getCurrentCompanyOrThrow();
        List<CustomerMinimalProjection> entities = customerRepository.findMinimalListByOwnerCompany(owner);
        return customerMapper.toMinimalDTOs(entities);
    }

    /**
     * Creates a new customer along with its associated company.
     *
     * @param dto the creation data transfer object containing customer and company info
     * @return the ID of the newly created customer
     */
    @Transactional
    @Override
    public CustomerForListDTO createCustomer(CreateCustomerRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Company companyEntity = companyMapper.toEntity(dto);
        companyEntity.setOwnerCompany(owner);
        Company savedCompany = companyRepository.save(companyEntity);

        if(dto.getDefaultVat()>1){
            dto.setDefaultVat(dto.getDefaultVat()/100);
        }

        if(dto.getDefaultWithholding()>1){
            dto.setDefaultWithholding(dto.getDefaultWithholding()/100);
        }
        Customer entity = customerMapper.toEntity(dto);
        entity.setCompany(savedCompany);
        entity.setOwnerCompany(owner);

        Customer customerSaved = customerRepository.save(entity);

        return customerMapper.toCustomerForListDTO(customerSaved);
    }

    /**
     * Updates an existing customer's data and its associated company data.
     *
     * @param id  the ID of the customer to update
     * @param dto the update data transfer object
     * @throws CustomerNotFoundException if the customer does not exist or is inaccessible
     */
    @Transactional
    @Override
    public void updateCustomer(int id, UpdateCustomerRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Customer entity = customerRepository.findByOwnerCompanyAndIdCustomer(owner, id)
                .orElseThrow(CustomerNotFoundException::new);

        Company company = entity.getCompany();

        if(dto.getDefaultVat()>1){
            dto.setDefaultVat(dto.getDefaultVat()/100);
        }

        if(dto.getDefaultWithholding()>1){
            dto.setDefaultWithholding(dto.getDefaultWithholding()/100);
        }

        companyMapper.updateEntity(company, dto);
        customerMapper.updateEntity(entity, dto);

        companyRepository.save(company);
        customerRepository.save(entity);
    }

    /**
     * Disables a customer, marking it as inactive.
     *
     * @param id the ID of the customer to disable
     * @throws CustomerNotFoundException if the customer does not exist or is inaccessible
     */
    @Transactional
    @Override
    public void disableCustomer(int id) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Customer entity = customerRepository.findByOwnerCompanyAndIdCustomer(owner, id)
                .orElseThrow(CustomerNotFoundException::new);
        entity.setEnabled(false);
        customerRepository.save(entity);
    }
}
