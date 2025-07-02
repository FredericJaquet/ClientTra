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

@RestController
@RequestMapping("/api/companies/{idCompany}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public List<AddressDTO> getAllAddresses(@PathVariable Integer idCompany) {
        return addressService.getAllAddresses(idCompany);
    }

    @GetMapping("/{idAddress}")
    public AddressDTO getAddressById(@PathVariable Integer idCompany, @PathVariable Integer idAddress) {
        return addressService.getAddress(idCompany, idAddress);
    }

    @DeleteMapping("/{idAddress}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteAddress(@PathVariable Integer idCompany, @PathVariable Integer idAddress) {
        addressService.deleteAddress(idCompany, idAddress);
        return ResponseEntity.ok(new GenericResponseDTO("address.deleted.success"));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createAddress(@PathVariable Integer idCompany,@Valid @RequestBody CreateAddressRequestDTO dto) {

        addressService.createAddress(idCompany, dto);
        return ResponseEntity.ok(new GenericResponseDTO("address.created.success"));
    }

    @PatchMapping("/{idAddress}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public AddressDTO updateAddress(@PathVariable Integer idCompany,@PathVariable Integer idAddress, @RequestBody UpdateAddressRequestDTO dto) {

        return addressService.updateAddress(idCompany, idAddress, dto);
    }

}
