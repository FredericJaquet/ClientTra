package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.*;
import com.frederic.clienttra.dto.create.CreateCustomerRequestDTO;
import com.frederic.clienttra.dto.read.CustomerDetailsDTO;
import com.frederic.clienttra.dto.read.CustomersForListDTO;
import com.frederic.clienttra.dto.update.UpdateCustomerRequestDTO;
import com.frederic.clienttra.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<CustomersForListDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createCustomer(@Valid @RequestBody CreateCustomerRequestDTO dto) {
        customerService.createCustomer(dto);
        return ResponseEntity.ok(new GenericResponseDTO("customer.created.success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailsDTO> getCustomerById(@PathVariable int id) {
        CustomerDetailsDTO customerDetails = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerDetails);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")//TODO ERROR, el error que lanza para access denied no es el correcto, es el generico.
    public ResponseEntity<CustomerDetailsDTO> updateCustomer(@PathVariable int id, @Valid @RequestBody UpdateCustomerRequestDTO dto) {
        customerService.updateCustomer(id, dto);
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @DeleteMapping("/{id}")//TODO ERROR, el error que lanza para ccess denied no es el correcto, es el generico.
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteCustomer(@PathVariable int id) {
        customerService.disableCustomer(id);
        return ResponseEntity.ok(new GenericResponseDTO("customer.deleted.success"));
    }

    @GetMapping("/search")
    public List<CustomersForListDTO> searchCustomers(@RequestParam String query) {
        return customerService.searchByNameOrVat(query);
    }
}
