package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.RegistrationActualCompanyRequestDTO;
import com.frederic.clienttra.services.DemoDataService;
import com.frederic.clienttra.services.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing demo data deletion and real company registration.
 * <p>
 * Provides endpoints to delete demo data and to register a real company,
 * accessible only to users with the ADMIN role.
 */
@RestController
@RequestMapping("/api/demo-data-deleting")
@RequiredArgsConstructor
public class DemoDataDeletingController {

    private final DemoDataService demoDataService;
    private final RegistrationService registrationService;

    /**
     * Deletes all demo data from the system.
     * Accessible only to users with ADMIN role.
     *
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/demo-data")
    public ResponseEntity<GenericResponseDTO> deleteDemoData() {
        demoDataService.deleteDemoData();
        return ResponseEntity.ok(new GenericResponseDTO("demo_data.deleted.success"));
    }

    /**
     * Registers a real company by replacing demo data.
     * Accessible only to users with ADMIN role.
     *
     * @param dto the registration data transfer object with real company info
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @PostMapping("/register-company")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GenericResponseDTO> registerRealCompany(@Valid @RequestBody RegistrationActualCompanyRequestDTO dto) {
        registrationService.registerFromDemo(dto);

        return ResponseEntity.ok(new GenericResponseDTO("registration.created.success"));
    }
}
