package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateProviderRequestDTO;
import com.frederic.clienttra.dto.read.BaseCompanyMinimalDTO;
import com.frederic.clienttra.dto.read.ProviderDetailsDTO;
import com.frederic.clienttra.dto.read.ProviderForListDTO;
import com.frederic.clienttra.dto.update.UpdateProviderRequestDTO;
import com.frederic.clienttra.services.ProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing providers.
 * <p>
 * Supports CRUD operations and searching/filtering providers.
 * Modification operations require ADMIN or ACCOUNTING roles.
 */
@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    /**
     * Retrieves all providers.
     *
     * @return a list of {@link ProviderForListDTO}
     */
    @GetMapping
    public ResponseEntity<List<ProviderForListDTO>> getAllProviders() {
        return ResponseEntity.ok(providerService.getAllProviders());
    }

    /**
     * Retrieves all providers filtered by enabled/disabled status.
     *
     * @param enabled true to get enabled providers, false otherwise
     * @return a list of filtered {@link ProviderForListDTO}
     */
    @GetMapping("/enabled")
    public ResponseEntity<List<ProviderForListDTO>> getAllProvidersEnabled(@RequestParam boolean enabled) {
        return ResponseEntity.ok(providerService.getAllProvidersEnabled(enabled));
    }

    /**
     * Retrieves provider details by ID.
     *
     * @param id the provider ID
     * @return the {@link ProviderDetailsDTO} for the specified provider
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProviderDetailsDTO> getProviderById(@PathVariable int id) {
        return ResponseEntity.ok(providerService.getProviderById(id));
    }

    /**
     * Searches providers by name or VAT.
     *
     * @param input the search input string
     * @return a list of matching {@link ProviderForListDTO}
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProviderForListDTO>> searchProvider(@RequestParam String input) {
        return ResponseEntity.ok(providerService.searchByNameOrVat(input));
    }

    /**
     * Retrieves a minimal list of providers for lightweight use.
     *
     * @return a list of {@link BaseCompanyMinimalDTO}
     */
    @GetMapping("minimal-list")
    public ResponseEntity<List<BaseCompanyMinimalDTO>> getMinimalProviders() {
        return ResponseEntity.ok(providerService.getMinimalProviderList());
    }

    /**
     * Creates a new provider.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param dto the creation request payload
     * @return a response with location of created provider and success message
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createProvider(@Valid @RequestBody CreateProviderRequestDTO dto) {
        int newId = providerService.createProvider(dto);

        return ResponseEntity
                .created(URI.create("/api/providers/" + newId))
                .body(new GenericResponseDTO("provider.created.success"));
    }

    /**
     * Updates an existing provider.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param id  the provider ID
     * @param dto the update request payload
     * @return the updated {@link ProviderDetailsDTO}
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<ProviderDetailsDTO> updateProvider(@PathVariable int id, @RequestBody UpdateProviderRequestDTO dto) {
        providerService.updateProvider(id, dto);
        return ResponseEntity.ok(providerService.getProviderById(id));
    }

    /**
     * Disables (soft deletes) a provider.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param id the provider ID
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteProvider(@PathVariable int id) {
        providerService.disableProvider(id);
        return ResponseEntity.ok(new GenericResponseDTO(("provider.deleted.success")));
    }

}
