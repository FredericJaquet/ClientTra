package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.CreateCustomerRequestDTO;
import com.frederic.clienttra.dto.CustomerDetailsDTO;
import com.frederic.clienttra.dto.CustomersForListDTO;
import com.frederic.clienttra.dto.UpdateCustomerDTO;

import java.util.List;

public interface CustomerService {
    List<CustomersForListDTO> getAllCustomers();
    CustomerDetailsDTO getCustomerById(int id);
    void createCustomer(CreateCustomerRequestDTO dto);
    void updateCustomer(int id, UpdateCustomerDTO dto);
    void disableCustomer(int id);
    List<CustomersForListDTO> searchByNameOrVat(String query);
}
