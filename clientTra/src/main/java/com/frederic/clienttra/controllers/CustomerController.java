package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.*;
import com.frederic.clienttra.dto.create.CreateCustomerRequestDTO;
import com.frederic.clienttra.dto.read.CustomerDetailsDTO;
import com.frederic.clienttra.dto.read.BaseCompanyMinimalDTO;
import com.frederic.clienttra.dto.read.CustomerForListDTO;
import com.frederic.clienttra.dto.read.ProviderForListDTO;
import com.frederic.clienttra.dto.update.UpdateCustomerRequestDTO;
import com.frederic.clienttra.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing customer companies.
 * <p>
 * Provides endpoints to list, search, retrieve, create, update, and disable customers.
 * Only users with ADMIN or ACCOUNTING roles can modify customer data.
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Retrieves a full list of all customers.
     *
     * @return a list of {@link CustomerForListDTO}
     */
    @GetMapping
    public ResponseEntity<List<CustomerForListDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    /**
     * Retrieves customers filtered by their "enabled" status.
     *
     * @param enabled boolean flag to filter enabled/disabled customers
     * @return a list of {@link CustomerForListDTO}
     */
    @GetMapping("/enabled")
    public ResponseEntity<List<CustomerForListDTO>> getAllCustomersEnabled(@RequestParam boolean enabled) {
        return ResponseEntity.ok(customerService.getAllCustomersEnabled(enabled));
    }

    /**
     * Retrieves full details of a customer by ID.
     *
     * @param id the ID of the customer
     * @return the corresponding {@link CustomerDetailsDTO}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailsDTO> getCustomerById(@PathVariable int id) {
        return ResponseEntity.ok(customerService.getCustomerByIdCompany(id));
    }

    /**
     * Searches customers by their name or VAT number.
     *
     * @param input the search input string
     * @return a list of matching {@link CustomerForListDTO}
     */
    @GetMapping("/search")
    public ResponseEntity<List<CustomerForListDTO>> searchCustomers(@RequestParam String input) {
        return ResponseEntity.ok(customerService.searchByNameOrVat(input));
    }

    /**
     * Returns a minimal list of customers for dropdowns or selection inputs.
     *
     * @return a list of {@link BaseCompanyMinimalDTO}
     */
    @GetMapping("/minimal-list")
    public ResponseEntity<List<BaseCompanyMinimalDTO>> getMinimalCustomers() {
        return ResponseEntity.ok(customerService.getMinimalCustomerList());
    }

    /**
     * Creates a new customer.
     * Only users with ADMIN or ACCOUNTING roles are allowed.
     *
     * @param dto the request payload containing customer data
     * @return a response with the location of the created customer and a success message
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<CustomerForListDTO> createCustomer(@Valid @RequestBody CreateCustomerRequestDTO dto) {
        CustomerForListDTO newDto = customerService.createCustomer(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(newDto);
    }

    /**
     * Updates an existing customer.
     * Only users with ADMIN or ACCOUNTING roles are allowed.
     * <p>
     *
     * @param idCompany  the ID of the customer to update
     * @param dto the update payload
     * @return the updated {@link CustomerDetailsDTO}
     */
    @PatchMapping("/{idCompany}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<CustomerDetailsDTO> updateCustomer(@PathVariable int idCompany, @RequestBody UpdateCustomerRequestDTO dto) {
        customerService.updateCustomer(idCompany, dto);
        return ResponseEntity.ok(customerService.getCustomerByIdCompany(idCompany));
    }

    /**
     * Disables (soft deletes) a customer by ID.
     * Only users with ADMIN or ACCOUNTING roles are allowed.
     * <p>
     *
     * @param id the ID of the customer to disable
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteCustomer(@PathVariable int id) {
        customerService.disableCustomer(id);
        return ResponseEntity.ok(new GenericResponseDTO("customer.deleted.success"));
    }

}
