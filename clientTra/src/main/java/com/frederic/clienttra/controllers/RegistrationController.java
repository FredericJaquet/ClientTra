package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateUserRequestDTO;
import com.frederic.clienttra.dto.create.RegistrationRequestDTO;
import com.frederic.clienttra.security.CustomUserDetails;
import com.frederic.clienttra.services.DemoDataService;
import com.frederic.clienttra.services.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller to handle company registration.
 * <p>
 * Supports registration of actual companies and demo companies with sample data.
 */
@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final DemoDataService demoDataService;

    /**
     * Registers a real (actual) company along with its admin user.
     * @param dto registration data for a real company
     * @return success message on creation
     */
    @PostMapping("/actual-data")
    public ResponseEntity<GenericResponseDTO> registerActualCompany(@Valid @RequestBody RegistrationRequestDTO dto) {
        registrationService.register(dto);
        return ResponseEntity.ok(new GenericResponseDTO("registration.created.success"));
    }

    /**
     * Registers a demo company loading sample/demo data.
     * @param dto user data to load demo data for
     * @return success message on creation with demo data
     */
    @PostMapping("/demo-data")
    public ResponseEntity<GenericResponseDTO> registerDemoCompany(@Valid @RequestBody CreateUserRequestDTO dto){
        demoDataService.loadData(dto);
        return ResponseEntity.ok(new GenericResponseDTO("registration.created_with_demo.success"));
    }
}
