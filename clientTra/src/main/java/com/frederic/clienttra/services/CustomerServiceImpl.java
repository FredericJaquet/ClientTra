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
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final CustomerMapper customerMapper;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    @Override
    public List<CustomerForListDTO> getAllCustomers() {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<CustomerListProjection> entities = new ArrayList<>(customerRepository.findListByOwnerCompany(owner));
        List<CustomerForListDTO> dtos=customerMapper.toCustomerForListDTOS(entities);
        dtos.sort(Comparator.comparing(CustomerForListDTO::getComName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

        return dtos;
    }

    @Override
    public List<CustomerForListDTO> searchByNameOrVat(String input) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        String query = "%" + input + "%";

        List<CustomerListProjection> entities = new ArrayList<>(customerRepository.findListByComNameOrLegalNameOrVatNumber(owner, query));
        List<CustomerForListDTO> dtos=customerMapper.toCustomerForListDTOS(entities);
        dtos.sort(Comparator.comparing(CustomerForListDTO::getComName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

        return dtos;
    }

    @Override
    public CustomerDetailsDTO getCustomerById(int id) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Customer entity = customerRepository.findByOwnerCompanyAndIdCustomer(owner, id)
                .orElseThrow(CustomerNotFoundException::new);
        return customerMapper.toCustomerDetailsDTO(entity);
    }

    @Override
    public int createCustomer(CreateCustomerRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Company companyEntity = companyMapper.toEntity(dto);
        companyEntity.setOwnerCompany(owner);

        Company savedCompany = companyRepository.save(companyEntity);

        Customer entity = customerMapper.toEntity(dto);
        entity.setCompany(savedCompany);
        entity.setOwnerCompany(owner);
        entity.setEnabled(true);

        Customer customerSaved=customerRepository.save(entity);

        return customerSaved.getIdCustomer();
    }

    @Override
    public void updateCustomer(int id, UpdateCustomerRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Customer entity = customerRepository.findByOwnerCompanyAndIdCustomer(owner,id)
                .orElseThrow(CustomerNotFoundException::new);

        Company company = entity.getCompany();

        companyMapper.updateEntity(company,dto);
        customerMapper.updateEntity(entity, dto);

        companyRepository.save(company);
        customerRepository.save(entity);
    }

    @Override
    public void disableCustomer(int id) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Customer entity = customerRepository.findByOwnerCompanyAndIdCustomer(owner,id)
                .orElseThrow(CustomerNotFoundException::new);
        entity.setEnabled(false);
        customerRepository.save(entity);
    }

    @Override
    public List<BaseCompanyMinimalDTO> getMinimalCustomerList() {
        Company owner = companyService.getCurrentCompanyOrThrow();
        List<CustomerMinimalProjection> entities = customerRepository.findMinimalListByOwnerCompany(owner);
        return customerMapper.toMinimalDTOs(entities);
    }

}


