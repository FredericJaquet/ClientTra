package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.RegistrationActualCompanyRequestDTO;
import com.frederic.clienttra.dto.create.RegistrationRequestDTO;
import com.frederic.clienttra.dto.demo.DemoCompanyDTO;
import com.frederic.clienttra.services.DemoDataService;
import com.frederic.clienttra.services.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demo-data-deleting")
@RequiredArgsConstructor
public class DemoDataDeletingController {

    private final DemoDataService demoDataService;
    private final RegistrationService registrationService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/demo-data")
    public ResponseEntity<GenericResponseDTO> deleteDemoData(){
        demoDataService.deleteDemoData();

        return ResponseEntity.ok(new GenericResponseDTO("demo_data.deleted.success"));
    }

    @PostMapping("/register-company")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GenericResponseDTO> registerRealCompany(@Valid @RequestBody RegistrationActualCompanyRequestDTO dto) {
        registrationService.registerFromDemo(dto);
        return ResponseEntity.ok(new GenericResponseDTO("registration.created.success"));
    }


}
