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

@RestController
@RequestMapping("/api/companies/{idCompany}/schemes")
@RequiredArgsConstructor
public class SchemeController {

    private final SchemeService schemeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<List<SchemeDTO>> getAllSchemes(@PathVariable Integer idCompany){
        return ResponseEntity.ok(schemeService.getAllSchemes(idCompany));
    }

    @GetMapping("/{idScheme}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<SchemeDTO> getSchemeById(@PathVariable Integer idCompany, @PathVariable Integer idScheme){
        return ResponseEntity.ok(schemeService.getScheme(idCompany, idScheme));
    }

    @DeleteMapping("/{idScheme}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteAddress(@PathVariable Integer idCompany, @PathVariable Integer idScheme){
        schemeService.deleteScheme(idCompany, idScheme);
        return ResponseEntity.ok(new GenericResponseDTO("scheme.deleted.success"));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createScheme(@PathVariable Integer idCompany, @Valid @RequestBody CreateSchemeRequestDTO dto){
        schemeService.createScheme(idCompany, dto);
        return ResponseEntity.ok(new GenericResponseDTO("scheme.created.success"));
    }

    @PatchMapping("/{idScheme}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<SchemeDTO> updateScheme(@PathVariable Integer idCompany, @PathVariable Integer idScheme, @RequestBody UpdateSchemeRequestDTO dto){

        return ResponseEntity.ok(schemeService.updateScheme(idCompany, idScheme, dto));
    }

}
