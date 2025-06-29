package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateCustomerRequestDTO;
import com.frederic.clienttra.dto.read.CustomerDetailsDTO;
import com.frederic.clienttra.dto.read.CustomersForListDTO;
import com.frederic.clienttra.dto.update.UpdateCustomerRequestDTO;

import java.util.List;

public interface CustomerService {
    List<CustomersForListDTO> getAllCustomers();
    CustomerDetailsDTO getCustomerById(int id);
    void createCustomer(CreateCustomerRequestDTO dto);
    void updateCustomer(int id, UpdateCustomerRequestDTO dto);
    void disableCustomer(int id);
    List<CustomersForListDTO> searchByNameOrVat(String query);
}
