package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateChangeRateRequestDTO;
import com.frederic.clienttra.dto.read.ChangeRateDTO;
import com.frederic.clienttra.dto.update.UpdateChangeRateRequestDTO;
import com.frederic.clienttra.services.ChangeRateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing currency exchange rates (change rates).
 * <p>
 * Provides endpoints to list, retrieve, create, update, and delete change rates.
 * Only users with ADMIN or ACCOUNTING roles can modify exchange rate data.
 */
@RestController
@RequestMapping("/api/change-rates")
@RequiredArgsConstructor
public class ChangeRateController {

    private final ChangeRateService changeRateService;

    /**
     * Retrieves all change rates available in the system.
     *
     * @return a list of {@link ChangeRateDTO}
     */
    @GetMapping
    public ResponseEntity<List<ChangeRateDTO>> getAllChangeRates() {
        return ResponseEntity.ok(changeRateService.getAllChangeRates());
    }

    /**
     * Retrieves a specific change rate by its ID.
     *
     * @param idChangeRate the ID of the change rate
     * @return the corresponding {@link ChangeRateDTO}
     */
    @GetMapping("/{idChangeRate}")
    public ResponseEntity<ChangeRateDTO> getChangeRateById(@PathVariable Integer idChangeRate) {
        return ResponseEntity.ok(changeRateService.getChangeRateById(idChangeRate));
    }

    /**
     * Creates a new change rate.
     * Only users with ADMIN or ACCOUNTING roles are allowed to perform this action.
     *
     * @param dto the change rate creation request
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<ChangeRateDTO> createChangeRate(@Valid @RequestBody CreateChangeRateRequestDTO dto) {
        ChangeRateDTO newChangeRate=changeRateService.createChangeRate(dto);
        return ResponseEntity.ok(newChangeRate);
    }

    /**
     * Updates an existing change rate by its ID.
     * Only users with ADMIN or ACCOUNTING roles are allowed to perform this action.
     *
     * @param idChangeRate the ID of the change rate to update
     * @param dto          the update request data
     * @return the updated {@link ChangeRateDTO}
     */
    @PatchMapping("/{idChangeRate}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<ChangeRateDTO> updateChangeRate(@PathVariable Integer idChangeRate, @RequestBody UpdateChangeRateRequestDTO dto) {
        return ResponseEntity.ok(changeRateService.updateChangeRate(idChangeRate, dto));
    }

    /**
     * Deletes a change rate by its ID.
     * Only users with ADMIN or ACCOUNTING roles are allowed to perform this action.
     *
     * @param idChangeRate the ID of the change rate to delete
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @DeleteMapping("/{idChangeRate}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteChangeRate(@PathVariable Integer idChangeRate) {
        changeRateService.deleteChangeRate(idChangeRate);
        return ResponseEntity.ok(new GenericResponseDTO("change_rate.deleted.success"));
    }
}
