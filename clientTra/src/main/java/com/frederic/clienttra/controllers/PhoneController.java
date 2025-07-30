package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreatePhoneRequestDTO;
import com.frederic.clienttra.dto.read.PhoneDTO;
import com.frederic.clienttra.dto.update.UpdatePhoneRequestDTO;
import com.frederic.clienttra.services.PhoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing phone numbers associated with a company.
 * <p>
 * Supports listing all phones, retrieving by ID, creating, updating, and deleting phones.
 * Modification operations require ADMIN or ACCOUNTING roles.
 */
@RestController
@RequestMapping("/api/companies/{idCompany}/phones")
@RequiredArgsConstructor
public class PhoneController {

    private final PhoneService phoneService;

    /**
     * Retrieves all phone entries for a given company.
     *
     * @param idCompany the company ID
     * @return a list of {@link PhoneDTO}
     */
    @GetMapping
    public ResponseEntity<List<PhoneDTO>> getAllPhones(@PathVariable Integer idCompany) {
        return ResponseEntity.ok(phoneService.getAllPhones(idCompany));
    }

    /**
     * Retrieves a phone entry by its ID for a given company.
     *
     * @param idCompany the company ID
     * @param idPhone   the phone ID
     * @return the {@link PhoneDTO} corresponding to the phone entry
     */
    @GetMapping("/{idPhone}")
    public ResponseEntity<PhoneDTO> getPhoneById(@PathVariable Integer idCompany, @PathVariable Integer idPhone) {
        return ResponseEntity.ok(phoneService.getPhone(idCompany, idPhone));
    }

    /**
     * Deletes a phone entry by its ID for a given company.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param idCompany the company ID
     * @param idPhone   the phone ID
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @DeleteMapping("/{idPhone}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteAddress(@PathVariable Integer idCompany, @PathVariable Integer idPhone) {
        phoneService.deletePhone(idCompany, idPhone);
        return ResponseEntity.ok(new GenericResponseDTO("phone.deleted.success"));
    }

    /**
     * Creates a new phone entry for a given company.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param idCompany the company ID
     * @param dto       the creation request payload
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createPhone(@PathVariable Integer idCompany, @Valid @RequestBody CreatePhoneRequestDTO dto) {
        phoneService.createPhone(idCompany, dto);
        return ResponseEntity.ok(new GenericResponseDTO("phone.created.success"));
    }

    /**
     * Updates an existing phone entry for a given company.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param idCompany the company ID
     * @param idPhone   the phone ID
     * @param dto       the update request payload
     * @return the updated {@link PhoneDTO}
     */
    @PatchMapping("/{idPhone}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<PhoneDTO> updatePhone(@PathVariable Integer idCompany, @PathVariable Integer idPhone, @RequestBody UpdatePhoneRequestDTO dto) {
        return ResponseEntity.ok(phoneService.updatePhone(idCompany, idPhone, dto));
    }
}
