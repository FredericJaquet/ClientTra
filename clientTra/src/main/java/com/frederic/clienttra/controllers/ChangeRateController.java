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

@RestController
@RequestMapping("/api/change-rates")
@RequiredArgsConstructor
public class ChangeRateController {

    private final ChangeRateService changeRateService;

    @GetMapping
    public ResponseEntity<List<ChangeRateDTO>> getAllChangeRates() {
        return ResponseEntity.ok(changeRateService.getAllChangeRates());
    }

    @GetMapping("/{idChangeRate}")
    public ResponseEntity<ChangeRateDTO> getChangeRateById(@PathVariable Integer idChangeRate) {
        return ResponseEntity.ok(changeRateService.getChangeRateById(idChangeRate));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createChangeRate(@Valid @RequestBody CreateChangeRateRequestDTO dto) {
        changeRateService.createChangeRate(dto);
        return ResponseEntity.ok(new GenericResponseDTO("change_rate.created.success"));
    }

    @PatchMapping("/{idChangeRate}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<ChangeRateDTO> updateChangeRate(@PathVariable Integer idChangeRate, @RequestBody UpdateChangeRateRequestDTO dto) {
        return ResponseEntity.ok(changeRateService.updateChangeRate(idChangeRate, dto));
    }

    @DeleteMapping("/{idChangeRate}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteChangeRate(@PathVariable Integer idChangeRate) {
        changeRateService.deleteChangeRate(idChangeRate);
        return ResponseEntity.ok(new GenericResponseDTO("change_rate.deleted.success"));
    }
}
