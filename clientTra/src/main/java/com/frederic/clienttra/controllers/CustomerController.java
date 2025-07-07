package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.*;
import com.frederic.clienttra.dto.create.CreateCustomerRequestDTO;
import com.frederic.clienttra.dto.read.CustomerDetailsDTO;
import com.frederic.clienttra.dto.read.BaseCompanyMinimalDTO;
import com.frederic.clienttra.dto.read.CustomerForListDTO;
import com.frederic.clienttra.dto.update.UpdateCustomerRequestDTO;
import com.frederic.clienttra.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {//TODO Crear endpoints para clientes habilitados y deshabilitados

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerForListDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createCustomer(@Valid @RequestBody CreateCustomerRequestDTO dto) {
        int newId = customerService.createCustomer(dto);
        /*CustomerDetailsDTO created = customerService.getCustomerById(newId);//Esto es un ejemplo de como devolver el nuevo cliente creado
        return ResponseEntity
                .created(URI.create("/api/customers/" + newId))
                .body(created);*/
        return ResponseEntity
                .created(URI.create("/api/customers/" + newId))
                .body(new GenericResponseDTO("customer.created.success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailsDTO> getCustomerById(@PathVariable int id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")//TODO ERROR, el error que lanza para access denied no es el correcto, es el generico.
    public ResponseEntity<CustomerDetailsDTO> updateCustomer(@PathVariable int id, @RequestBody UpdateCustomerRequestDTO dto) {
        customerService.updateCustomer(id, dto);
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @DeleteMapping("/{id}")//TODO ERROR, el error que lanza para access denied no es el correcto, es el generico.
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteCustomer(@PathVariable int id) {
        customerService.disableCustomer(id);
        return ResponseEntity.ok(new GenericResponseDTO("customer.deleted.success"));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerForListDTO>> searchCustomers(@RequestParam String input) {
        return ResponseEntity.ok(customerService.searchByNameOrVat(input));
    }

    @GetMapping("/minimal-list")
    public ResponseEntity<List<BaseCompanyMinimalDTO>> getMinimalCustomers() {
        return ResponseEntity.ok(customerService.getMinimalCustomerList());
    }
}
