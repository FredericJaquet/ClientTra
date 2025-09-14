package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateCustomerRequestDTO;
import com.frederic.clienttra.dto.read.CustomerDetailsDTO;
import com.frederic.clienttra.dto.read.BaseCompanyMinimalDTO;
import com.frederic.clienttra.dto.read.CustomerForListDTO;
import com.frederic.clienttra.dto.update.UpdateCustomerRequestDTO;

import java.util.List;

/**
 * Service interface for managing customers.
 * <p>
 * Provides methods for retrieving, searching, creating, updating,
 * and disabling customer entities.
 * </p>
 */
public interface CustomerService {
    List<CustomerForListDTO> getAllCustomers();
    List<CustomerForListDTO> getAllCustomersEnabled(boolean enabled);
    CustomerDetailsDTO getCustomerById(int id);
    List<CustomerForListDTO> searchByNameOrVat(String query);
    List<BaseCompanyMinimalDTO> getMinimalCustomerList();
    CustomerForListDTO createCustomer(CreateCustomerRequestDTO dto);
    void updateCustomer(int id, UpdateCustomerRequestDTO dto);
    void disableCustomer(int id);

}
