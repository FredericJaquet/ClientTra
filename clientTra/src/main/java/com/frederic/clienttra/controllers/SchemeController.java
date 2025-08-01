package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateSchemeRequestDTO;
import com.frederic.clienttra.dto.read.SchemeDTO;
import com.frederic.clienttra.dto.update.UpdateSchemeRequestDTO;
import com.frederic.clienttra.services.SchemeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing billing schemes within a company.
 * <p>
 * Supports CRUD operations with role-based access control (ADMIN, ACCOUNTING).
 */
@RestController
@RequestMapping("/api/companies/{idCompany}/schemes")
@RequiredArgsConstructor
public class SchemeController {

    private final SchemeService schemeService;

    /**
     * Retrieves all schemes for a given company.
     * Requires ADMIN or ACCOUNTING role.
     *
     * @param idCompany the ID of the company
     * @return list of schemes
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<List<SchemeDTO>> getAllSchemes(@PathVariable Integer idCompany){
        return ResponseEntity.ok(schemeService.getAllSchemes(idCompany));
    }

    /**
     * Retrieves a specific scheme by ID within a company.
     * Requires ADMIN or ACCOUNTING role.
     *
     * @param idCompany the ID of the company
     * @param idScheme the ID of the scheme
     * @return the scheme details
     */
    @GetMapping("/{idScheme}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<SchemeDTO> getSchemeById(@PathVariable Integer idCompany, @PathVariable Integer idScheme){
        return ResponseEntity.ok(schemeService.getScheme(idCompany, idScheme));
    }

    /**
     * Deletes a scheme logically within a company.
     * Requires ADMIN or ACCOUNTING role.
     *
     * @param idCompany the ID of the company
     * @param idScheme the ID of the scheme to delete
     * @return success message
     */
    @DeleteMapping("/{idScheme}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteScheme(@PathVariable Integer idCompany, @PathVariable Integer idScheme){
        schemeService.deleteScheme(idCompany, idScheme);
        return ResponseEntity.ok(new GenericResponseDTO("scheme.deleted.success"));
    }

    /**
     * Creates a new scheme within a company.
     * Requires ADMIN or ACCOUNTING role.
     *
     * @param idCompany the ID of the company
     * @param dto the data for the new scheme
     * @return success message
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createScheme(@PathVariable Integer idCompany, @Valid @RequestBody CreateSchemeRequestDTO dto){
        schemeService.createScheme(idCompany, dto);
        return ResponseEntity.ok(new GenericResponseDTO("scheme.created.success"));
    }

    /**
     * Updates an existing scheme within a company.
     * Requires ADMIN or ACCOUNTING role.
     *
     * @param idCompany the ID of the company
     * @param idScheme the ID of the scheme to update
     * @param dto data to update the scheme
     * @return the updated scheme details
     */
    @PatchMapping("/{idScheme}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<SchemeDTO> updateScheme(@PathVariable Integer idCompany, @PathVariable Integer idScheme, @RequestBody UpdateSchemeRequestDTO dto){
        return ResponseEntity.ok(schemeService.updateScheme(idCompany, idScheme, dto));
    }
}
