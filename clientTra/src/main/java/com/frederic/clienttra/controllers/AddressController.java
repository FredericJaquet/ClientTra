package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateAddressRequestDTO;
import com.frederic.clienttra.dto.read.AddressDTO;
import com.frederic.clienttra.dto.update.UpdateAddressRequestDTO;
import com.frederic.clienttra.services.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing addresses associated with a specific company.
 * <p>
 * Provides endpoints to list, retrieve, create, update, and delete addresses.
 * Access to modification endpoints is restricted to users with roles ADMIN or ACCOUNTING.
 */
@RestController
@RequestMapping("/api/companies/{idCompany}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * Retrieves all addresses belonging to the specified company.
     *
     * @param idCompany the ID of the company
     * @return a list of {@link AddressDTO}
     */
    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses(@PathVariable Integer idCompany) {
        return ResponseEntity.ok(addressService.getAllAddresses(idCompany));
    }

    /**
     * Retrieves a single address by its ID, scoped to the specified company.
     *
     * @param idCompany the ID of the company
     * @param idAddress the ID of the address
     * @return the {@link AddressDTO} for the requested address
     */
    @GetMapping("/{idAddress}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Integer idCompany, @PathVariable Integer idAddress) {
        return ResponseEntity.ok(addressService.getAddress(idCompany, idAddress));
    }

    /**
     * Deletes an address by its ID. Only users with ADMIN or ACCOUNTING roles are allowed.
     *
     * @param idCompany the ID of the company
     * @param idAddress the ID of the address to delete
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @DeleteMapping("/{idAddress}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteAddress(@PathVariable Integer idCompany, @PathVariable Integer idAddress) {
        addressService.deleteAddress(idCompany, idAddress);
        return ResponseEntity.ok(new GenericResponseDTO("address.deleted.success"));
    }

    /**
     * Creates a new address for the specified company.
     * Only users with ADMIN or ACCOUNTING roles are allowed.
     *
     * @param idCompany the ID of the company
     * @param dto       the address creation request
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createAddress(@PathVariable Integer idCompany, @Valid @RequestBody CreateAddressRequestDTO dto) {
        addressService.createAddress(idCompany, dto);
        return ResponseEntity.ok(new GenericResponseDTO("address.created.success"));
    }

    /**
     * Updates an existing address by its ID.
     * Only users with ADMIN or ACCOUNTING roles are allowed.
     *
     * @param idCompany the ID of the company
     * @param idAddress the ID of the address to update
     * @param dto       the address update request
     * @return the updated {@link AddressDTO}
     */
    @PatchMapping("/{idAddress}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Integer idCompany, @PathVariable Integer idAddress, @RequestBody UpdateAddressRequestDTO dto) {
        return ResponseEntity.ok(addressService.updateAddress(idCompany, idAddress, dto));
    }
}
